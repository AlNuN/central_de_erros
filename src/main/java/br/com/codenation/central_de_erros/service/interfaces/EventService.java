package br.com.codenation.central_de_erros.service.interfaces;

import br.com.codenation.central_de_erros.entity.Event;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface EventService extends ServiceInterface<Event> {
    Optional<Event> findById(Long id);

    Page<Event> findAll(Pageable pageable);

    void delete(Long id);
}
