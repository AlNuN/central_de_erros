package br.com.codenation.central_de_erros.controller.Users;

import org.hamcrest.Matchers;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
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
public class PostSaveUserIntegrationTests {

    @Autowired
    private MockMvc mvc;

    @Test
    public void shouldSaveNew() throws Exception {
        mvc.perform(MockMvcRequestBuilders.post("/users")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content("{" +
                        "\"email\": \"test@test.com\",\n" +
                        "\"password\": \"12345\"\n" +
                        "}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.email", Matchers.equalTo("test@test.com")));
    }

    @Test
    public void shouldGetBadRequestForDuplicatedEmail() throws Exception {
        mvc.perform(MockMvcRequestBuilders.post("/users")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content("{" +
                        "\"email\": \"test@test.com\",\n" +
                        "\"password\": \"12345\"\n" +
                        "}"));

        mvc.perform(MockMvcRequestBuilders.post("/users")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content("{" +
                        "\"email\": \"test@test.com\",\n" +
                        "\"password\": \"12345\"\n" +
                        "}"))
        .andExpect(status().isBadRequest());
    }

    @Test
    public void shouldGetBadRequestIfTryToSetId() throws Exception {
        mvc.perform(MockMvcRequestBuilders.post("/users")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content("{" +
                        "\"id\": 10,\n" +
                        "\"email\": \"test@test.com\",\n" +
                        "\"password\": \"12345\"\n" +
                        "}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void shouldGetBadRequestForWrongEmailFormat() throws Exception {
        mvc.perform(MockMvcRequestBuilders.post("/users")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content("{" +
                        "\"email\": \"test\",\n" +
                        "\"password\": \"12345\"\n" +
                        "}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void shouldGetBadRequestForNotNullField() throws Exception {
        mvc.perform(MockMvcRequestBuilders.post("/users")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content("{" +
                        "\"email\": \"test@test.com\",\n" +
                        "}"))
                .andExpect(status().isBadRequest());
    }
}
