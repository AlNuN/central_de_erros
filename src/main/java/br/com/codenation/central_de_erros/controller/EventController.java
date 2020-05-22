package br.com.codenation.central_de_erros.controller;

import br.com.codenation.central_de_erros.assembler.EventResourceAssembler;
import br.com.codenation.central_de_erros.entity.Event;
import br.com.codenation.central_de_erros.exception.EventNotFoundException;
import br.com.codenation.central_de_erros.service.interfaces.EventService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.PagedResources;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.Resources;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/events")
public class EventController {

    private final EventService eventService;
    private final EventResourceAssembler eventAssembler;

    @GetMapping
    public ResponseEntity<Resources<Resource<Event>>> all(Pageable pageable) {
        List<Resource<Event>> events = eventService.findAll(pageable).stream()
                .map(eventAssembler::toResource)
                .collect(Collectors.toList());
        return ResponseEntity.ok( new Resources<>(events,
                linkTo(methodOn(EventController.class).all(pageable)).withSelfRel()));
    }

    @GetMapping("{id}")
    public ResponseEntity<Resource<Event>> one (@PathVariable Long id) {
        Event event = eventService.findById(id)
                .orElseThrow(()-> new EventNotFoundException(id));
        return ResponseEntity.ok(eventAssembler.toResource(event));

    }

    @PostMapping
    public ResponseEntity<Resource<Event>> newEvent(@RequestBody Event newEvent)
                                                throws URISyntaxException {
            Resource<Event> resource = eventAssembler.toResource(eventService.save(newEvent));
            return ResponseEntity.created(new URI(resource.getId().expand().getHref())).body(resource);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Resource<Event>> change(@RequestBody Event newEvent,
            @PathVariable Long id) throws URISyntaxException {
        Event updatedEvent = eventService.findById(id)
                .map(event -> {
                    event.setDateTime(newEvent.getDateTime());
                    event.setErrorType(newEvent.getErrorType());
                    event.setLog(newEvent.getLog());
                    event.setDescription(newEvent.getDescription());
                    event.setOrigin(newEvent.getOrigin());
                    event.setRepeated(newEvent.getRepeated());
                    return eventService.save(event);
                })
                .orElseGet(() -> {
                    newEvent.setId(id);
                    return eventService.save(newEvent);
                });

        Resource<Event> resource = eventAssembler.toResource(updatedEvent);
        return ResponseEntity.created(new URI(resource.getId().expand().getHref())).body(resource);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id){
        eventService.findById(id)
                .orElseThrow(() -> new EventNotFoundException(id));
        eventService.delete(id);
        return ResponseEntity.ok("Event with id: " + id + " removed with success");
    }

}
