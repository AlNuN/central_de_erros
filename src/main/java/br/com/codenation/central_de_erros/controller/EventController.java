package br.com.codenation.central_de_erros.controller;

import br.com.codenation.central_de_erros.assembler.EventMapper;
import br.com.codenation.central_de_erros.assembler.EventResourceLogMapper;
import br.com.codenation.central_de_erros.assembler.SuccessJSON;
import br.com.codenation.central_de_erros.entity.Event;
import br.com.codenation.central_de_erros.exception.EventNotFoundException;
import br.com.codenation.central_de_erros.exception.WrongUserInputException;
import br.com.codenation.central_de_erros.resources.EventResource;
import br.com.codenation.central_de_erros.resources.EventResourceWithLog;
import br.com.codenation.central_de_erros.service.interfaces.EventServiceInterface;
import lombok.RequiredArgsConstructor;
import net.kaczmarzyk.spring.data.jpa.domain.Equal;
import net.kaczmarzyk.spring.data.jpa.domain.Like;
import net.kaczmarzyk.spring.data.jpa.web.annotation.And;
import net.kaczmarzyk.spring.data.jpa.web.annotation.Spec;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.PagedResources;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.ResourceAssembler;
import org.springframework.hateoas.mvc.ControllerLinkBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/events")
public class EventController {

    private final EventServiceInterface eventService;
    private final ResourceAssembler<Event, EventResource> eventResourceAssembler;
    private final EventResourceLogMapper eventResourceLogMapper;
    private final EventMapper eventMapper;

    @GetMapping
    public ResponseEntity<PagedResources<EventResource>> all(@And({
                                                                     @Spec(path = "level", spec = Equal.class),
                                                                     @Spec(path = "description", spec = Like.class),
                                                                     @Spec(path = "origin", spec = Like.class),
                                                                     @Spec(path = "log", spec = Like.class),
                                                                     @Spec(path = "dateTime", spec = Equal.class,
                                                                             config = "yyyy-MM-dd HH:mm"),
                                                                     @Spec(path = "number", spec = Equal.class)
                                                             }) Specification<Event> eventSpec,
                                                             Pageable pageable,
                                                             PagedResourcesAssembler<Event> pagedResourceAssembler) {

        Page<Event> events = eventService.findAll(eventSpec, pageable);

        Link selfLink = new Link(ServletUriComponentsBuilder.fromCurrentRequest().build().toUriString());
        PagedResources<EventResource> result = pagedResourceAssembler.toResource(events, eventResourceAssembler, selfLink);
        return ResponseEntity.ok(result);
    }

    @GetMapping("{id}")
    public ResponseEntity<EventResourceWithLog> one (@PathVariable Long id) {
        Event event = eventService.findById(id)
                .orElseThrow(()-> new EventNotFoundException(id));
        return ResponseEntity.ok(eventResourceLogMapper.map(event));
    }

    @PostMapping
    public ResponseEntity<EventResourceWithLog> newEvent(@Valid @RequestBody Event newEvent)
                                                throws URISyntaxException {
            EventResourceWithLog resource = eventResourceLogMapper.map(eventService.saveNew(newEvent));
            return ResponseEntity.created(new URI(resource.getId().expand().getHref())).body(resource);
    }

    @PutMapping("/{id}")
    public ResponseEntity<EventResourceWithLog> update(@Valid @RequestBody Event newEvent,
            @PathVariable Long id) throws URISyntaxException {

        if (newEvent.getId() != null && newEvent.getId() != id){
            throw new WrongUserInputException("URI id (" + id + ") and body id (" +
                    newEvent.getId() + ") diverge.");
        }

        Event updatedEvent = eventService.findById(id)
                .map(event -> eventService.save(eventMapper.map(event, newEvent)))
                .orElseGet(() -> eventService.saveNew(newEvent));

        EventResourceWithLog resource = eventResourceLogMapper.map(updatedEvent);
        return ResponseEntity.created(new URI(resource.getId().expand().getHref())).body(resource);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Resource<SuccessJSON>> delete(@PathVariable Long id){
        eventService.findById(id)
                .orElseThrow(() -> new EventNotFoundException(id));
        eventService.delete(id);

        Resource<SuccessJSON> resource = new Resource<>(new SuccessJSON("Event with id " + id + " removed with success"));
        resource.add(ControllerLinkBuilder.linkTo(EventController.class) .withRel("events"));

        return ResponseEntity.ok(resource);
    }

}
