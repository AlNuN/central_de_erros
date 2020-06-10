package br.com.codenation.central_de_erros.service.Impl;

import br.com.codenation.central_de_erros.entity.Event;
import br.com.codenation.central_de_erros.repository.EventRepository;
import br.com.codenation.central_de_erros.service.interfaces.EventServiceInterface;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class EventService implements EventServiceInterface {

    private final EventRepository repository;

    @Override
    public Event save(Event event){
        return repository.save(event);
    }

    @Override
    public Event saveNew(Event event) {
        return repository.findByLevelAndDescriptionAndLogAndOrigin(
                event.getLevel(), event.getDescription(),
                event.getLog(), event.getOrigin()
        )
                .map(e -> {
                    e.setNumber(e.getNumber() + event.getNumber());
                    return repository.save(e);
                })
                .orElseGet(() -> repository.save(event));
    }

    @Override
    public Optional<Event> findById(Long id) {
        return repository.findById(id);
    }

    @Override
    public Page<Event> findAll(Specification<Event> eventSpec, Pageable pageable) {
        return repository.findAll(eventSpec, pageable);
    }

    @Override
    public void delete(Long id) { repository.deleteById(id); }

}
