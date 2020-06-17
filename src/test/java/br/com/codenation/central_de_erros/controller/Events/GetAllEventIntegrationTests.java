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

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@Sql({"/tests.sql"})
@ActiveProfiles(value = "noAuth")
public class GetAllEventIntegrationTests {

    @Autowired
    private MockMvc mvc;

    @Test
    @WithMockUser(roles="ADMIN")
    public void shouldGetAllResources() throws Exception {
        mvc.perform(get("/events")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.events", hasSize(7)));
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
        mvc.perform(get("/events?level=INFO")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.events", hasSize(2)))
                .andExpect(jsonPath("$._embedded.events[0].id", equalTo(3)));
    }

    @Test
    @WithMockUser(roles="ADMIN")
    public void shouldReturnBadRequestWithMeaningfulMessageForWrongInfo() throws Exception {
        mvc.perform(get("/events?level=I")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", Matchers.containsString("could not find value I for enum")));
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
    public void shouldGetByDateTimeInApril() throws Exception {
        mvc.perform(get("/events?dateTimeAfter=2020-04-01 00:00&dateTimeBefore=2020-05-01 00:00")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.events", hasSize(2)))
                .andExpect(jsonPath("$._embedded.events[0].id", equalTo(2)))
                .andExpect(jsonPath("$._embedded.events[1].id", equalTo(6)));
    }

    @Test
    @WithMockUser(roles="ADMIN")
    public void shouldGetBadRequestForOnly1ParamForBetweenDateWithMeaningfulMessage() throws Exception {
        mvc.perform(get("/events?dateTimeBefore=2020-01-01 00:00")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", Matchers.containsString("expected 2")));
    }

    @Test
    @WithMockUser(roles="ADMIN")
    public void shouldGetByDateTimeBefore2020() throws Exception {
        mvc.perform(get("/events?dateTimePre=2020-01-01 00:00")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.events", hasSize(2)))
                .andExpect(jsonPath("$._embedded.events[0].id", equalTo(4)))
                .andExpect(jsonPath("$._embedded.events[1].id", equalTo(7)));
    }

    @Test
    @WithMockUser(roles="ADMIN")
    public void shouldGetByDateTimeAfter2021() throws Exception {
        mvc.perform(get("/events?dateTimePos=2021-01-01 00:00")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.events", hasSize(1)))
                .andExpect(jsonPath("$._embedded.events[0].id", equalTo(5)));
    }

    @Test
    @WithMockUser(roles="ADMIN")
    public void shouldReturnBadRequestForWrongDateTimeFormat() throws Exception {
        mvc.perform(get("/events?dateTime=2020-5-23 9:38")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(roles="ADMIN")
    public void shouldGetByIdenticalNumber() throws Exception {
        mvc.perform(get("/events?number=2")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.events", hasSize(2)));
    }

    @Test
    @WithMockUser(roles="ADMIN")
    public void shouldGetBadRequestWithMeaningfulMessageForInvalidNumberInput() throws Exception {
        mvc.perform(get("/events?number=a")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", Matchers.containsString("number format exception")));
    }

    @Test
    @WithMockUser(roles="ADMIN")
    public void shouldGetWarningsWithDepreInLogSortedByDescendentId() throws Exception {
        mvc.perform(get("/events?level=WARNING&log=depre&sort=id,desc")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.events", hasSize(2)))
                .andExpect(jsonPath("$._embedded.events[0].id", equalTo(5)))
                .andExpect(jsonPath("$._embedded.events[1].id", equalTo(1)));
    }

    @Test
    @WithMockUser(roles="ADMIN")
    public void shouldReturnAllForWrongQuery() throws Exception {
        mvc.perform(get("/events?a")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.events", hasSize(7)));
    }

}
