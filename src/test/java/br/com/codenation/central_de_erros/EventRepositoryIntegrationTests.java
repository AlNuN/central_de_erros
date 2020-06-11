package br.com.codenation.central_de_erros;

import br.com.codenation.central_de_erros.entity.Event;
import br.com.codenation.central_de_erros.entity.Level;
import br.com.codenation.central_de_erros.repository.EventRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.auditing.AuditingHandler;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.junit.Assert.*;

@ContextConfiguration(classes=CentralDeErrosApplication.class)
@RunWith(SpringRunner.class)
@SpringBootTest
public class EventRepositoryIntegrationTests {

    @Autowired
    private EventRepository eventRepository;

    @MockBean
    private AuditingHandler auditingHandler;  // ignore auditing fields

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

    @Test
    public void shouldReturnEqual(){
        Event event = eventRepository.findByLevelAndDescriptionAndLogAndOrigin(
                Level.INFO, "Deixa eu te falar uma coisa",
                "Tô executando essa parada aqui", "spring"
        ).orElse(null);
        assertEquals(event3, event);
    }

    @Test
    public void shouldReturnAll(){
        Specification<Event> specs = null;
        List<Event> events = eventRepository.findAll(specs , Pageable.unpaged()).getContent();
        List<Event> repository = Arrays.asList(event3, event2, event1);
        assertThat(repository, containsInAnyOrder(events.toArray()));
    }

    @Test
    public void shouldReturnOnlyWithSameLevel(){
        Specification<Event> specs = (Specification<Event>) (root, criteriaQuery, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("level"), Level.INFO);
        List<Event> events = eventRepository.findAll(specs , Pageable.unpaged()).getContent();
        List<Event> repository = Arrays.asList(event3);
        assertThat(repository, containsInAnyOrder(events.toArray()));
    }

    @Test
    public void shouldReturnWithDescriptionAlike(){
        Specification<Event> specs = (Specification<Event>) (root, criteriaQuery, criteriaBuilder) ->
                criteriaBuilder.like(root.get("description"), "%ruim%");
        List<Event> events = eventRepository.findAll(specs, Pageable.unpaged()).getContent();
        List<Event> repository = Arrays.asList(event1, event2);
        assertArrayEquals(repository.toArray(), events.toArray());
    }


}
