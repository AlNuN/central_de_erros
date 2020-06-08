package br.com.codenation.central_de_erros;

import br.com.codenation.central_de_erros.entity.Event;
import br.com.codenation.central_de_erros.entity.Level;
import br.com.codenation.central_de_erros.repository.EventRepository;
import br.com.codenation.central_de_erros.service.Impl.EventService;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.LocalDateTime;

import static org.hamcrest.Matchers.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class EventRestControllerIntegrationTests {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private EventService eventService;

    @Autowired
    private EventRepository eventRepository;

    private Event event1;
    private Event event2;
    private Event event3;

    @Before
    public void EventRepositoryIntegrationTests (){
        event1 = new Event(
                1L, Level.WARNING, "Deu mais ou menos ruim",
                "Function slaoq deprecated", "front-end",
                LocalDateTime.of(2020,05,23,9,38),
                0L, LocalDateTime.of(2020,05,23,9,38));
        event2 = new Event(
                2L, Level.ERROR, "O código está errado, ruim",
                "Arithmetic Exception: division by 0", "back-end",
                LocalDateTime.of(2020,04,05,13,45),
                10L, LocalDateTime.of(2020,04,05,13,45));
        event3 = new Event(
                3L, Level.INFO, "Deixa eu te falar uma coisa",
                "Tô executando essa parada aqui", "spring",
                LocalDateTime.of(2020,06,02,16,03),
                2L, LocalDateTime.of(2020,06,02,16,03));
        eventRepository.save(event1);
        eventRepository.save(event2);
        eventRepository.save(event3);
    }


    // GET All()

    @Test
    @WithMockUser(roles="ADMIN")
    public void shouldGetAllResources() throws Exception {
        mvc.perform(get("/events")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.events", hasSize(3)));
    }

    @Test
    @WithMockUser(roles="ADMIN")
    public void shouldGetPaginatedResources() throws Exception {
        mvc.perform(get("/events?page=1&size=1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.events", hasSize(1)))
                .andExpect(jsonPath("$._links", hasKey("first")));
    }

    @Test
    @WithMockUser(roles="ADMIN")
    public void shouldGetSortedResources() throws Exception {
        mvc.perform(get("/events?page=0&size=1&sort=level,asc")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.events[0].id", equalTo(2)));
    }

    @Test
    @WithMockUser(roles="ADMIN")
    public void shouldGetOnlyInfos() throws Exception {
        mvc.perform(get("/events?level=I")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.events", hasSize(1)))
                .andExpect(jsonPath("$._embedded.events[0].id", equalTo(3)));
    }

    @Test
    @WithMockUser(roles="ADMIN")
    public void shouldReturnBadRequestForWrongInfo() throws Exception {
        mvc.perform(get("/events?level=In")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(roles="ADMIN")
    public void shouldGetByDescriptionContaining() throws Exception {
        mvc.perform(get("/events?description=ruim")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.events", hasSize(2)));
    }

    @Test
    @WithMockUser(roles="ADMIN")
    public void shouldGetByOriginContaining() throws Exception {
        mvc.perform(get("/events?origin=end")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.events", hasSize(2)));
    }

    @Test
    @WithMockUser(roles="ADMIN")
    public void shouldGetByLogContaining() throws Exception {
        mvc.perform(get("/events?log=ion")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.events", hasSize(2)));
    }

    @Test
    @WithMockUser(roles="ADMIN")
    public void shouldGetByIdenticalDateTime() throws Exception {
        mvc.perform(get("/events?dateTime=2020-05-23 09:38")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.events", hasSize(1)));
    }

    @Test
    @WithMockUser(roles="ADMIN")
    public void shouldGetBadRequestForWrongDateTimeFormat() throws Exception {
        mvc.perform(get("/events?dateTime=2020-5-23 9:38")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(roles="ADMIN")
    public void shouldGetByIdenticalRepeatedTimes() throws Exception {
        mvc.perform(get("/events?repeated=2")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.events", hasSize(1)));
    }

    @Test
    @WithMockUser(roles="ADMIN")
    public void shouldGetBadRequestForInvalidRepeatedInput() throws Exception {
        mvc.perform(get("/events?repeated=a")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    // GET one()

    @Test
    @WithMockUser(roles="ADMIN")
    public void shouldGetEventByID() throws Exception {
        mvc.perform(get("/events/2")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.level", Matchers.equalTo("ERROR")));
    }

    @Test
    @WithMockUser(roles="ADMIN")
    public void ShouldReturnNotFoundForWrongId() throws Exception {
        Integer eventId = 10;
        mvc.perform(get("/events/" + eventId)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message", Matchers.equalTo("Could not find Event " + eventId)));
    }

    @Test
    @WithMockUser(roles="ADMIN")
    public void shouldAcceptOnlyNumbers() throws Exception {
        mvc.perform(get("/events/a")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    // POST newEvent()

    @Test
    @WithMockUser(roles="ADMIN")
    public void shouldSaveNew() throws Exception {
        mvc.perform(MockMvcRequestBuilders.post("/events")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content("{" +
                        "\"level\": \"INFO\"," +
                        "\"description\": \"Info de fora\"," +
                        "\"origin\": \"spring\",\n" +
                        "\"dateTime\": \"2020-06-01 15:36\",\n" +
                        "\"repeated\": 8,\n" +
                        "\"log\": \"Aqui tem informação!\"\n" +
                        "}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", Matchers.equalTo(4)));
    }

    @Test
    @WithMockUser(roles="ADMIN")
    public void postShouldModifyWithID() throws Exception {
        mvc.perform(MockMvcRequestBuilders.post("/events")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content("{" +
                        "\"id\": 1," +
                        "\"level\": \"INFO\"," +
                        "\"description\": \"Info de fora\"," +
                        "\"origin\": \"spring\",\n" +
                        "\"dateTime\": \"2020-06-01 15:36\",\n" +
                        "\"repeated\": 8,\n" +
                        "\"log\": \"Aqui tem informação!\"\n" +
                        "}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", Matchers.equalTo(1)))
                .andExpect(jsonPath("$.level", Matchers.equalTo("INFO")));
    }

    @Test
    @WithMockUser(roles="ADMIN")
    public void postShouldReturnBadRequestWhen() throws Exception {
        // Missing field notNull (level)
        mvc.perform(MockMvcRequestBuilders.post("/events")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content("{" +
                        "\"description\": \"Info de fora\"," +
                        "\"origin\": \"spring\",\n" +
                        "\"dateTime\": \"2020-06-01 15:36\",\n" +
                        "\"repeated\": 8,\n" +
                        "\"log\": \"Aqui tem informação!\"\n" +
                        "}"))
                .andExpect(status().isBadRequest());

        // Wrong field name
        mvc.perform(MockMvcRequestBuilders.post("/events")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content("{" +
                        "\"lvl\": \"INFO\"," +  // level
                        "\"description\": \"Info de fora\"," +
                        "\"origin\": \"spring\",\n" +
                        "\"dateTime\": \"2020-06-01 15:36\",\n" +
                        "\"repeated\": 8,\n" +
                        "\"log\": \"Aqui tem informação!\"\n" +
                        "}"))
                .andExpect(status().isBadRequest());

        // Wrong field contents
        mvc.perform(MockMvcRequestBuilders.post("/events")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content("{" +
                        "\"level\": \"W\"," +  // should be WARNING, INFO or Error
                        "\"description\": \"Info de fora\"," +
                        "\"origin\": \"spring\",\n" +
                        "\"dateTime\": \"2020-06-01 15:36\",\n" +
                        "\"repeated\": 8,\n" +
                        "\"log\": \"Aqui tem informação!\"\n" +
                        "}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(roles="ADMIN")
    public void shouldSumFieldsIfRepeated() throws Exception {
        mvc.perform(MockMvcRequestBuilders.post("/events")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content("{" +
                        "\"level\": \"INFO\"," +
                        "\"description\": \"Deixa eu te falar uma coisa\"," +
                        "\"origin\": \"spring\",\n" +
                        "\"dateTime\": \"2020-06-01 15:36\",\n" +
                        "\"repeated\": 8,\n" +
                        "\"log\": \"Tô executando essa parada aqui\"\n" +
                        "}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", Matchers.equalTo(3)))
                .andExpect(jsonPath("$.repeated", Matchers.equalTo(10)))
        ;
    }

    @Test
    @WithMockUser(roles="ADMIN")
    public void shouldJustIncrementFieldIfRepeatedIs0() throws Exception {
        mvc.perform(MockMvcRequestBuilders.post("/events")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content("{" +
                        "\"level\": \"INFO\"," +
                        "\"description\": \"Deixa eu te falar uma coisa\"," +
                        "\"origin\": \"spring\",\n" +
                        "\"dateTime\": \"2020-06-01 15:36\",\n" +
                        "\"repeated\": 0,\n" +
                        "\"log\": \"Tô executando essa parada aqui\"\n" +
                        "}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", Matchers.equalTo(3)))
                .andExpect(jsonPath("$.repeated", Matchers.equalTo(3)))
        ;
    }
    // PUT change()

    @Test
    @WithMockUser(roles="ADMIN")
    public void shouldModifyIfIdExists() throws Exception {
        mvc.perform(MockMvcRequestBuilders.put("/events/1")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content("{" +
                        "\"level\": \"INFO\"," +
                        "\"description\": \"Info de fora\"," +
                        "\"origin\": \"spring\",\n" +
                        "\"dateTime\": \"2020-06-01 15:36\",\n" +
                        "\"repeated\": 8,\n" +
                        "\"log\": \"Aqui tem informação!\"\n" +
                        "}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", Matchers.equalTo(1)))
                .andExpect(jsonPath("$.level", Matchers.equalTo("INFO")));
    }

    @Test
    @WithMockUser(roles="ADMIN")
    public void shouldSaveNewIfIdDoesNotExistsWithNextId() throws Exception {
        mvc.perform(MockMvcRequestBuilders.put("/events/20")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content("{" +
                        "\"level\": \"INFO\"," +
                        "\"description\": \"Info de fora\"," +
                        "\"origin\": \"spring\",\n" +
                        "\"dateTime\": \"2020-06-01 15:36\",\n" +
                        "\"repeated\": 8,\n" +
                        "\"log\": \"Aqui tem informação!\"\n" +
                        "}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", Matchers.equalTo(4)))
                .andExpect(jsonPath("$.level", Matchers.equalTo("INFO")));
    }

    @Test
    @WithMockUser(roles="ADMIN")
    public void shouldReturnBadRequestIfIdDiverge() throws Exception {
        mvc.perform(MockMvcRequestBuilders.put("/events/2")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content("{" +
                        "\"id\": 3," +
                        "\"level\": \"INFO\"," +
                        "\"description\": \"Info de fora\"," +
                        "\"origin\": \"spring\",\n" +
                        "\"dateTime\": \"2020-06-01 15:36\",\n" +
                        "\"repeated\": 8,\n" +
                        "\"log\": \"Aqui tem informação!\"\n" +
                        "}"))
                .andExpect(status().isBadRequest());
    }

    // DELETE delete()
    @Test
    @WithMockUser(roles="ADMIN")
    public void shouldDeleteIfIdExists() throws Exception {
        mvc.perform(MockMvcRequestBuilders.delete("/events/1")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles="ADMIN")
    public void shouldReturnNotFoundIfIdDoesNotExists() throws Exception {
        mvc.perform(MockMvcRequestBuilders.delete("/events/11")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
}
