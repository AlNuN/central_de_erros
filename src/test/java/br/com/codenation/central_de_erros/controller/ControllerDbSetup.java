package br.com.codenation.central_de_erros.controller;

import br.com.codenation.central_de_erros.entity.Event;
import br.com.codenation.central_de_erros.entity.Level;
import br.com.codenation.central_de_erros.repository.EventRepository;
import org.junit.Before;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;

public class ControllerDbSetup {

    @Autowired
    protected EventRepository eventRepository;

    protected Event event1;
    protected Event event2;
    protected Event event3;
    protected Event event4;
    protected Event event5;
    protected Event event6;
    protected Event event7;

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
        event4 = new Event(
                4L, Level.ERROR, "Alguma coisa deu errado",
                "Um erro desconhecido", "DB",
                LocalDateTime.of(2019,8,05,16,03),
                1L, LocalDateTime.of(2020,07,15,3,47));
        event5 = new Event(
                5L, Level.WARNING, "Apenas um aviso",
                "Algo está deprecado", "spring",
                LocalDateTime.of(2021,1,06,2,03),
                2L, LocalDateTime.of(2020,07,15,3,47));
        event6 = new Event(
                6L, Level.INFO, "Conexão com o banco de dados bem-sucedida",
                "DB connected", "DB",
                LocalDateTime.of(2020,4,23,9,45),
                3L, LocalDateTime.of(2020,07,15,3,47));
        event7 = new Event(
                7L, Level.WARNING, "CUIDADO!",
                "cuidado", "cuidado",
                LocalDateTime.of(2019,12,25,10,11),
                4L, LocalDateTime.of(2020,07,15,3,47));
        eventRepository.save(event1);
        eventRepository.save(event2);
        eventRepository.save(event3);
        eventRepository.save(event4);
        eventRepository.save(event5);
        eventRepository.save(event6);
        eventRepository.save(event7);
    }

}
