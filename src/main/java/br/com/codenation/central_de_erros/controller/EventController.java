package br.com.codenation.central_de_erros.controller;

import br.com.codenation.central_de_erros.assembler.EventResourceAssembler;
import br.com.codenation.central_de_erros.entity.Event;
import br.com.codenation.central_de_erros.exception.EventNotFoundException;
import br.com.codenation.central_de_erros.service.interfaces.EventService;
import lombok.RequiredArgsConstructor;
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
    public ResponseEntity<Resources<Resource<Event>>> all() {
        List<Resource<Event>> events = eventService.findAll().stream()
                .map(eventAssembler::toResource)
                .collect(Collectors.toList());
        return ResponseEntity.ok( new Resources<>(events, linkTo(methodOn(EventController.class).all()).withSelfRel()) );
    }

    @GetMapping("{id}")
    public ResponseEntity<Resource<Event>> one (@PathVariable Long id) {
        Event event = eventService.findById(id)
                .orElseThrow(()-> new EventNotFoundException(id));
        return ResponseEntity.ok(eventAssembler.toResource(event));

    }

    @PostMapping
    public ResponseEntity<Resource<Event>> newEvent(@RequestBody Event newEvent) throws URISyntaxException {
        Resource<Event> resource = eventAssembler.toResource(eventService.save(newEvent));

        return ResponseEntity.created(new URI(resource.getId().expand().getHref())).body(resource);
    }

}
