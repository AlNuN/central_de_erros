package br.com.codenation.central_de_erros.service.interfaces;

import br.com.codenation.central_de_erros.entity.Event;

import java.util.List;
import java.util.Optional;

public interface EventService extends ServiceInterface<Event> {
    Optional<Event> findById(Long id);

    List<Event> findAll();
}
