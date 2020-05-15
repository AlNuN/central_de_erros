package br.com.codenation.central_de_erros.service;

import br.com.codenation.central_de_erros.entity.Event;
import br.com.codenation.central_de_erros.repository.EventRepository;
import br.com.codenation.central_de_erros.service.interfaces.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class EventServiceImpl implements EventService {
    @Autowired
    private EventRepository repository;

    @Override
    public Event save(Event object) {
        return repository.save(object);
    }

    @Override
    public Optional<Event> findById(Long id) {
        return repository.findById(id);
    }


}
