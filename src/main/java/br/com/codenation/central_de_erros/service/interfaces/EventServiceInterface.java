package br.com.codenation.central_de_erros.service.interfaces;

import br.com.codenation.central_de_erros.entity.Level;
import br.com.codenation.central_de_erros.entity.Event;
import br.com.codenation.central_de_erros.entity.LevelConverter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.Optional;

public interface EventServiceInterface extends ServiceInterface<Event> {
    Optional<Event> findById(Long id);

    Page<Event> findAll(Pageable pageable);

    Page<Event> findByLevel(String level, Pageable pageable, LevelConverter levelConverter);

    Page<Event> findByDescription(String description, Pageable pageable);

    Page<Event> findByOrigin(String origin, Pageable pageable);

    Page<Event> findByLog(String log, Pageable pageable);

    Page<Event> findByDateTime(String date, Pageable pageable);

    Page<Event> findByRepeated(String repeatedString, Pageable pageable);

    void delete(Long id);
}
