package br.com.codenation.central_de_erros.repository;

import br.com.codenation.central_de_erros.entity.Level;
import br.com.codenation.central_de_erros.entity.Event;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;


public interface EventRepository extends JpaRepository<Event, Long> {
    Page<Event> findAll(Pageable pageable);
    Page<Event> findByLevel(Level level, Pageable pageable);
    Page<Event> findByDescriptionContaining(String description, Pageable pageable);
    Page<Event> findByOriginContaining(String origin, Pageable pageable);
    Page<Event> findByLogContaining(String log, Pageable pageable);
    Page<Event> findByDateTime(LocalDateTime dateTime, Pageable pageable);
    Page<Event> findByRepeated(Long repeated, Pageable pageable);
}
