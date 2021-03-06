package br.com.codenation.central_de_erros.controller.Events;

import org.hamcrest.Matchers;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@Sql({"/tests.sql"})
@ActiveProfiles(value = "noAuth")
public class PostNewEventIntegrationTests {
    @Autowired
    private MockMvc mvc;

    @Test
    public void shouldSaveNew() throws Exception {
        mvc.perform(MockMvcRequestBuilders.post("/events")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content("{" +
                        "\"level\": \"INFO\"," +
                        "\"description\": \"Info de fora\"," +
                        "\"origin\": \"spring\",\n" +
                        "\"dateTime\": \"2020-06-01 15:36\",\n" +
                        "\"number\": 8,\n" +
                        "\"log\": \"Aqui tem informação!\"\n" +
                        "}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", Matchers.equalTo(8)));
    }

    @Test
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
                        "\"number\": 8,\n" +
                        "\"log\": \"Aqui tem informação!\"\n" +
                        "}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", Matchers.equalTo(1)))
                .andExpect(jsonPath("$.level", Matchers.equalTo("INFO")));
    }

    @Test
    public void postShouldReturnBadRequestWhenMissingNotNullFieldLevel() throws Exception {
        mvc.perform(MockMvcRequestBuilders.post("/events")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content("{" +
                        "\"description\": \"Info de fora\"," +
                        "\"origin\": \"spring\",\n" +
                        "\"dateTime\": \"2020-06-01 15:36\",\n" +
                        "\"number\": 8,\n" +
                        "\"log\": \"Aqui tem informação!\"\n" +
                        "}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void postShouldReturnBadRequestWhenUseWrongFieldName() throws Exception {
        mvc.perform(MockMvcRequestBuilders.post("/events")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content("{" +
                        "\"lvl\": \"INFO\"," +  // level
                        "\"description\": \"Info de fora\"," +
                        "\"origin\": \"spring\",\n" +
                        "\"dateTime\": \"2020-06-01 15:36\",\n" +
                        "\"number\": 8,\n" +
                        "\"log\": \"Aqui tem informação!\"\n" +
                        "}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void postShouldReturnBadRequestWhenUseWrongFieldValue() throws Exception {
        mvc.perform(MockMvcRequestBuilders.post("/events")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content("{" +
                        "\"level\": \"W\"," +  // should be WARNING, INFO or Error
                        "\"description\": \"Info de fora\"," +
                        "\"origin\": \"spring\",\n" +
                        "\"dateTime\": \"2020-06-01 15:36\",\n" +
                        "\"number\": 8,\n" +
                        "\"log\": \"Aqui tem informação!\"\n" +
                        "}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void shouldSumFieldsIfEqual() throws Exception {
        mvc.perform(MockMvcRequestBuilders.post("/events")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content("{" +
                        "\"level\": \"INFO\"," +
                        "\"description\": \"Deixa eu te falar uma coisa\"," +
                        "\"origin\": \"spring\",\n" +
                        "\"dateTime\": \"2020-06-01 15:36\",\n" +
                        "\"number\": 8,\n" +
                        "\"log\": \"Tô executando essa parada aqui\"\n" +
                        "}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", Matchers.equalTo(3)))
                .andExpect(jsonPath("$.number", Matchers.equalTo(10)))
        ;
    }

    @Test
    public void shouldSumFieldIfNumberIs0() throws Exception {
        mvc.perform(MockMvcRequestBuilders.post("/events")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content("{" +
                        "\"level\": \"INFO\"," +
                        "\"description\": \"Deixa eu te falar uma coisa\"," +
                        "\"origin\": \"spring\",\n" +
                        "\"dateTime\": \"2020-06-01 15:36\",\n" +
                        "\"number\": 0,\n" +
                        "\"log\": \"Tô executando essa parada aqui\"\n" +
                        "}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", Matchers.equalTo(3)))
                .andExpect(jsonPath("$.number", Matchers.equalTo(2)))
        ;
    }

}
