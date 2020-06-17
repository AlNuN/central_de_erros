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
import static br.com.codenation.central_de_erros.controller.Users.AcessToken.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class GetLoggedInUser {
    @Autowired
    private MockMvc mvc;

    @Test
    public void shouldGetLoggedInUser() throws Exception {
        mvc.perform(MockMvcRequestBuilders.post("/users")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content("{" +
                        "\"email\": \"test@test.com\",\n" +
                        "\"password\": \"0000\"\n" +
                        "}"));

        String token = obtainAccessToken("test@test.com", "0000", mvc);
        mvc.perform(MockMvcRequestBuilders.get("/users")
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email", Matchers.equalTo("test@test.com")));
    }

}
