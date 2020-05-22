package br.com.codenation.central_de_erros.service;

import br.com.codenation.central_de_erros.entity.Event;
import br.com.codenation.central_de_erros.repository.EventRepository;
import br.com.codenation.central_de_erros.service.interfaces.EventService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {

    private final EventRepository repository;

    @Override
    public Event save(Event object) {
        object.setCreatedAt(LocalDateTime.now());
        return repository.save(object);
    }

    @Override
    public Optional<Event> findById(Long id) {
        return repository.findById(id);
    }

    @Override
    public List<Event> findAll(Pageable pageable) { return repository.findAll(pageable).getContent(); }

    @Override
    public void delete(Long id) { repository.deleteById(id); }

}
