package br.com.codenation.central_de_erros.controller.Users;

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

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class Oauth2IntegrationTests {
    @Autowired
    private MockMvc mvc;

    @Test
    public void shouldNotHaveAcessToGetEvents() throws Exception{
        mvc.perform(MockMvcRequestBuilders.get("/events")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }

    @Test
    public void shouldNotHaveAcessToGetEvent() throws Exception{
        mvc.perform(MockMvcRequestBuilders.get("/events/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }

    @Test
    public void shouldNotHaveAcessToPostEvent() throws Exception{
        mvc.perform(MockMvcRequestBuilders.post("/events")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }

    @Test
    public void shouldNotHaveAcessToPutEvent() throws Exception{
        mvc.perform(MockMvcRequestBuilders.put("/events/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }

    @Test
    public void shouldNotHaveAcessToDeleteEvent() throws Exception{
        mvc.perform(MockMvcRequestBuilders.delete("/events/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }

    @Test
    public void shouldHaveAcessToSwaggerUi() throws Exception{
        mvc.perform(MockMvcRequestBuilders.get("/swagger-ui.html")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void shouldHaveAcessToDocs() throws Exception{
        mvc.perform(MockMvcRequestBuilders.get("/v2/api-docs")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void shouldNotHaveAcessToGetUsers() throws Exception{
        mvc.perform(MockMvcRequestBuilders.get("/users")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }

    @Test
    public void shouldNotHaveAcessToPutUsers() throws Exception{
        mvc.perform(MockMvcRequestBuilders.put("/users")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }
}
