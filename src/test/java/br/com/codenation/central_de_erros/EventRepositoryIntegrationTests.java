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
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.collection.IsIterableContainingInAnyOrder.containsInAnyOrder;
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
        List<Event> repository = Arrays.asList(event3, event2, event1);
        assertEquals(event3, event);
    }

    @Test
    public void shouldReturnAll(){
        List<Event> events = eventRepository.findAll(Pageable.unpaged()).getContent();
        List<Event> repository = Arrays.asList(event3, event2, event1);
        assertThat(repository, containsInAnyOrder(events.toArray()));
    }

    @Test
    public void shouldReturnOnlyWithSameLevel(){
        List<Event> events = eventRepository.findByLevel(Level.INFO, Pageable.unpaged()).getContent();
        List<Event> repository = Arrays.asList(event3);
        assertArrayEquals(repository.toArray(), events.toArray());
    }

    @Test
    public void shouldReturnWithDescriptionAlike(){
        List<Event> events = eventRepository.findByDescriptionContaining("ruim", Pageable.unpaged()).getContent();
        List<Event> repository = Arrays.asList(event1, event2);
        assertArrayEquals(repository.toArray(), events.toArray());
    }

    @Test
    public void shouldReturnWithOriginAlike(){
        List<Event> events = eventRepository.findByOriginContaining("ing", Pageable.unpaged()).getContent();
        List<Event> repository = Arrays.asList(event3);
        assertArrayEquals(repository.toArray(), events.toArray());
    }

    @Test
    public void shouldReturnWithLogAlike(){
        List<Event> events = eventRepository.findByLogContaining("0", Pageable.unpaged()).getContent();
        List<Event> repository = Arrays.asList(event2);
        assertArrayEquals(repository.toArray(), events.toArray());
    }

    @Test
    public void shouldReturnIdenticalDate(){
        List<Event> events = eventRepository.findByDateTime(LocalDateTime.of(2020,06,02,16,03), Pageable.unpaged()).getContent();
        List<Event> repository = Arrays.asList(event3);
        assertArrayEquals(repository.toArray(), events.toArray());
    }

    @Test
    public void shouldReturnIdenticalNumber(){
        List<Event> events = eventRepository.findByNumber(0L, Pageable.unpaged()).getContent();
        List<Event> repository = Arrays.asList(event1);
        assertArrayEquals(repository.toArray(), events.toArray());
    }
}
