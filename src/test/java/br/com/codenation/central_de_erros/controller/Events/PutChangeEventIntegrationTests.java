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
public class PutChangeEventIntegrationTests {
    @Autowired
    private MockMvc mvc;

    @Test
    public void shouldModifyIfIdExists() throws Exception {
        mvc.perform(MockMvcRequestBuilders.put("/events/1")
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
                .andExpect(jsonPath("$.id", Matchers.equalTo(1)))
                .andExpect(jsonPath("$.level", Matchers.equalTo("INFO")))
                .andExpect(jsonPath("$.number", Matchers.equalTo(8)));

        mvc.perform(MockMvcRequestBuilders.put("/events/1")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content("{" +
                        "\"level\": \"INFO\"," +
                        "\"description\": \"Info de fora\"," +
                        "\"origin\": \"spring\",\n" +
                        "\"dateTime\": \"2020-06-01 15:36\",\n" +
                        "\"number\": 10,\n" +
                        "\"log\": \"Aqui tem informação!\"\n" +
                        "}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", Matchers.equalTo(1)))
                .andExpect(jsonPath("$.level", Matchers.equalTo("INFO")))
                .andExpect(jsonPath("$.number", Matchers.equalTo(10)));
    }

    @Test
    public void shouldReturnBadRequestIfIdDoesNotExists() throws Exception {
        mvc.perform(MockMvcRequestBuilders.put("/events/20")
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
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", Matchers.equalTo("Event with id '20' does not exists")));
    }

    @Test
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
                        "\"number\": 8,\n" +
                        "\"log\": \"Aqui tem informação!\"\n" +
                        "}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void shouldReturnBadRequestIfWrongLevelValue() throws Exception {
        mvc.perform(MockMvcRequestBuilders.put("/events/1")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content("{" +
                        "\"level\": \"I\"" +
                        "}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void shouldReturnBadRequestIfWrongURIIdFormat() throws Exception {
        mvc.perform(MockMvcRequestBuilders.put("/events/a")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content("{" +
                        "\"level\": \"INFO\"" +
                        "}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void shouldNotUpdateWrongKeyName() throws Exception {
        mvc.perform(MockMvcRequestBuilders.put("/events/1")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content("{" +
                        "\"evel\": \"INFO\"" +
                        "}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", Matchers.equalTo(1)))
                .andExpect(jsonPath("$.level", Matchers.equalTo("WARNING")))
                .andExpect(jsonPath("$.number", Matchers.equalTo(0)))
                .andExpect(jsonPath("$.log", Matchers.equalTo("Function slaoq deprecated")))
                .andExpect(jsonPath("$.origin", Matchers.equalTo("front-end")))
                .andExpect(jsonPath("$.description", Matchers.equalTo("Deu mais ou menos ruim")));
    }

    @Test
    public void shouldModifyOnlySentFieldsAndKeepOthersEqual() throws Exception {
        mvc.perform(MockMvcRequestBuilders.put("/events/1")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content("{" +
                        "\"level\": \"INFO\"" +
                        "}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", Matchers.equalTo(1)))
                .andExpect(jsonPath("$.level", Matchers.equalTo("INFO")))
                .andExpect(jsonPath("$.number", Matchers.equalTo(0)))
                .andExpect(jsonPath("$.log", Matchers.equalTo("Function slaoq deprecated")))
                .andExpect(jsonPath("$.origin", Matchers.equalTo("front-end")))
                .andExpect(jsonPath("$.description", Matchers.equalTo("Deu mais ou menos ruim")));
    }
}
