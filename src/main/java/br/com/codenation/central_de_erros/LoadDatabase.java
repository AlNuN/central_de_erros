package br.com.codenation.central_de_erros;

import br.com.codenation.central_de_erros.entity.ErrorTypes;
import br.com.codenation.central_de_erros.entity.Event;
import br.com.codenation.central_de_erros.repository.EventRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDateTime;

@Configuration
@Slf4j
public class LoadDatabase {

    @Bean
    CommandLineRunner initDatabase(EventRepository repository){
        return args -> {
            log.info("Preloading " + repository.save(new Event(
                    1L, ErrorTypes.WARNING, "Deu mais ou menos ruim",
                    "Function slaoq deprecated", "front-end",
                    LocalDateTime.of(2020,05,23,9,38),
                    0L, LocalDateTime.now()
            )));
            log.info("Preloading " + repository.save(new Event(
                    2L, ErrorTypes.ERROR, "O código tá errado fiao",
                    "Arithmetic Exception: division by 0", "back-end",
                    LocalDateTime.of(2020,04,05,13,45),
                    10L, LocalDateTime.now()
            )));
            log.info("Preloading " + repository.save(new Event(
                    3L, ErrorTypes.INFO, "Deixa eu te falar uma coisa",
                    "Tô executando essa parada aqui", "spring",
                    LocalDateTime.of(2020,06,02,16,03),
                    2L, LocalDateTime.now()
            )));
        };
    }
}
