package br.com.codenation.central_de_erros.service.interfaces;

import br.com.codenation.central_de_erros.entity.Event;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.Optional;

public interface EventServiceInterface extends ServiceInterface<Event> {

    Event saveNew(Event event);

    Optional<Event> findById(Long id);

    Page<Event> findAll(Specification<Event> eventSpec, Pageable pageable);

    void delete(Long id);
}
