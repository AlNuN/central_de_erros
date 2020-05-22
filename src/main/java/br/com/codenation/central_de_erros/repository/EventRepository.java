package br.com.codenation.central_de_erros.repository;

import br.com.codenation.central_de_erros.entity.Event;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EventRepository extends JpaRepository<Event, Long> {
    Page<Event> findAll(Pageable pageable);
}
