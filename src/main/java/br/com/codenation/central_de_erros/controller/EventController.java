package br.com.codenation.central_de_erros.controller;

import br.com.codenation.central_de_erros.assembler.EventResourceMapper;
import br.com.codenation.central_de_erros.entity.LevelConverter;
import br.com.codenation.central_de_erros.entity.Event;
import br.com.codenation.central_de_erros.exception.EventNotFoundException;
import br.com.codenation.central_de_erros.resources.EventResource;
import br.com.codenation.central_de_erros.service.interfaces.EventServiceInterface;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.PagedResources;
import org.springframework.hateoas.ResourceAssembler;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDateTime;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/events")
public class EventController {

    private final EventServiceInterface eventService;
    private final ResourceAssembler<Event, EventResource> eventResourceAssembler;

    @GetMapping
    public ResponseEntity<PagedResources<EventResource>> all(@RequestParam Optional<String> level,
                                                             @RequestParam Optional<String> description,
                                                             @RequestParam Optional<String> origin,
                                                             @RequestParam Optional<String> log,
                                                             @RequestParam Optional<String> dateTime,
                                                             @RequestParam Optional<String> repeated,
                                                             Pageable pageable,
                                                             PagedResourcesAssembler<Event> pagedResourceAssembler,
                                                             LevelConverter typeConverter) {

        Page<Event> events = level.map(l -> eventService.findByLevel(l, pageable, typeConverter))
                .orElseGet(() -> description.map(d -> eventService.findByDescription(d, pageable))
                        .orElseGet(() -> origin.map(o -> eventService.findByOrigin(o, pageable))
                                .orElseGet(() -> log.map(l -> eventService.findByLog(l, pageable))
                                        .orElseGet(() -> dateTime.map(d -> eventService.findByDateTime(d, pageable))
                                                .orElseGet(() -> repeated.map(r -> eventService.findByRepeated(r, pageable))
                                                        .orElse(eventService.findAll(pageable)))))));

        Link selfLink = new Link(ServletUriComponentsBuilder.fromCurrentRequest().build().toUriString());
        PagedResources<EventResource> result = pagedResourceAssembler.toResource(events, eventResourceAssembler, selfLink);
        return ResponseEntity.ok(result);
    }

    @GetMapping("{id}")
    public ResponseEntity<EventResource> one (@PathVariable Long id) {
        Event event = eventService.findById(id)
                .orElseThrow(()-> new EventNotFoundException(id));
        return ResponseEntity.ok(EventResourceMapper.INSTANCE.map(event));

    }

    @PostMapping
    public ResponseEntity<EventResource> newEvent(@RequestBody Event newEvent)
                                                throws URISyntaxException {
            EventResource resource = EventResourceMapper.INSTANCE.map(eventService.save(newEvent));
            return ResponseEntity.created(new URI(resource.getId().expand().getHref())).body(resource);
    }

    @PutMapping("/{id}")
    public ResponseEntity<EventResource> change(@RequestBody Event newEvent,
            @PathVariable Long id) throws URISyntaxException {
        Event updatedEvent = eventService.findById(id)
                .map(event -> {
                    event.setDateTime(newEvent.getDateTime());
                    event.setLevel(newEvent.getLevel());
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

        EventResource resource = EventResourceMapper.INSTANCE.map(updatedEvent);
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
