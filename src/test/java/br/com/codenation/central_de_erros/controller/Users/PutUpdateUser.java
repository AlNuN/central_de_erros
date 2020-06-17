package br.com.codenation.central_de_erros.controller.Users;

import org.hamcrest.Matchers;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.json.JacksonJsonParser;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static br.com.codenation.central_de_erros.controller.Users.AcessToken.obtainAccessToken;
import static org.junit.Assert.assertTrue;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@Sql({"/users.sql"})
public class PutUpdateUser {
    @Autowired
    private MockMvc mvc;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    public void shouldUpdateUserEmailAndPassword() throws Exception {
        String token = obtainAccessToken("test@test.com", "123", mvc);
        mvc.perform(MockMvcRequestBuilders.put("/users")
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{" +
                        "\"email\": \"new@mail.com\",\n" +
                        "\"password\": \"12345\"\n" +
                        "}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.email", Matchers.equalTo("new@mail.com")));

        String newToken = obtainAccessToken("new@mail.com", "12345", mvc);
        mvc.perform(MockMvcRequestBuilders.get("/users")
                .header("Authorization", "Bearer " + newToken)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email", Matchers.equalTo("new@mail.com")));

        mvc.perform(MockMvcRequestBuilders.get("/users")
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }

    @Test
    public void shouldUpdateUserEmailOnly() throws Exception {
        String token = obtainAccessToken("test@test.com", "123", mvc);
        MvcResult result = mvc.perform(MockMvcRequestBuilders.put("/users")
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{" +
                        "\"email\": \"new@mail.com\" " +
                        "}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.email", Matchers.equalTo("new@mail.com")))
                .andReturn();
        String password = new JacksonJsonParser().parseMap(
                result.getResponse().getContentAsString()).get("password").toString();
        assertTrue(passwordEncoder.matches("123", password));
    }

    @Test
    public void shouldNotUpdateUserEmailIfFieldNameWrong() throws Exception {
        String token = obtainAccessToken("test@test.com", "123", mvc);
        mvc.perform(MockMvcRequestBuilders.put("/users")
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{" +
                        "\"mail\": \"new@mail.com\" " +
                        "}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.email", Matchers.equalTo("test@test.com")));
    }

    @Test
    public void shouldReturnBadRequestIfEmailFormatWrong() throws Exception {
        String token = obtainAccessToken("test@test.com", "123", mvc);
        mvc.perform(MockMvcRequestBuilders.put("/users")
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{" +
                        "\"email\": \"new\" " +
                        "}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void shouldReturnBadRequestIfEmailExists() throws Exception {
        String token = obtainAccessToken("test@test.com", "123", mvc);
        mvc.perform(MockMvcRequestBuilders.put("/users")
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{" +
                        "\"email\": \"mat@mat.com\" " +
                        "}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void shouldReturnBadRequestIfTryToProvideId() throws Exception {
        String token = obtainAccessToken("test@test.com", "123", mvc);
        mvc.perform(MockMvcRequestBuilders.put("/users")
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{" +
                        "\"id\": 1, " +
                        "\"email\": \"newmail@mail.com\" " +
                        "}"))
                .andExpect(status().isBadRequest());
    }
}
