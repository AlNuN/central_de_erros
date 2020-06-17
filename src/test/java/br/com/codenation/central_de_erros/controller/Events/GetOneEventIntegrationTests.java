package br.com.codenation.central_de_erros.controller;

import org.hamcrest.Matchers;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@Sql({"/tests.sql"})
@ActiveProfiles(value = "noAuth")
public class GetOneEventIntegrationTests {

    @Autowired
    private MockMvc mvc;

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

}
