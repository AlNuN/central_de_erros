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
import io.swagger.annotations.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
    @ApiOperation("List events allowing pagination, sorting and filtering.")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Resources found"),
            @ApiResponse(code = 400, message = "Wrong enum; using DateTimeAfter without DateTimeBefore; DateTime format not yyyy-MM-dd HH:mm"),
            @ApiResponse(code = 401, message = "Unauthorized"),
    })
    @ApiImplicitParams({
            @ApiImplicitParam(name = "level", dataType = "String", paramType = "query", value = "WARNING, INFO or ERROR"),
            @ApiImplicitParam(name = "description", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "origin", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "log", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "dateTime", dataType = "date", paramType = "query", value = "yyyy-MM-dd HH:mm"),
            @ApiImplicitParam(name = "dateTimeAfter", dataType = "date", paramType = "query", value = "yyyy-MM-dd HH:mm - should be used with dateTimeBefore"),
            @ApiImplicitParam(name = "dateTimeBefore", dataType = "date", paramType = "query", value = "yyyy-MM-dd HH:mm - should be used with dateTimeAfter"),
            @ApiImplicitParam(name = "dateTimePre", dataType = "date", paramType = "query", value = "yyyy-MM-dd HH:mm"),
            @ApiImplicitParam(name = "dateTimePos", dataType = "date", paramType = "query", value = "yyyy-MM-dd HH:mm"),
            @ApiImplicitParam(name = "number", dataType = "int", paramType = "query", example = "2")
    })
    public ResponseEntity<PagedResources<EventResource>> all (EventSpec eventSpec,
                                                             Pageable pageable,
                                                             PagedResourcesAssembler<Event> pagedResourceAssembler) {

        Page<Event> events = eventService.findAll(eventSpec, pageable);

        Link selfLink = new Link(ServletUriComponentsBuilder.fromCurrentRequest().build().toUriString());
        PagedResources<EventResource> result = pagedResourceAssembler.toResource(events, eventResourceAssembler, selfLink);
        return ResponseEntity.ok(result);
    }

    @GetMapping("{id}")
    @ApiOperation("Find one event by id and shows its log.")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Event with id x found"),
            @ApiResponse(code = 400, message = "Invalid value for id e.g. a letter"),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 404, message = "Event id not found"),
    })
    public ResponseEntity<EventResourceWithLog> one (@PathVariable Long id) {
        Event event = eventService.findById(id)
                .orElseThrow(()-> new EventNotFoundException(id));
        return ResponseEntity.ok(eventResourceLogMapper.map(event));
    }

    @PostMapping
    @ApiOperation("Save new event.")
    @ApiResponses({
            @ApiResponse(code = 201, message = "Created new resource or modified if set existing id"),
            @ApiResponse(code = 400, message = "Missing not null field; wrong field name; wrong field value"),
            @ApiResponse(code = 401, message = "Unauthorized"),
    })
    public ResponseEntity<EventResourceWithLog> newEvent (@Valid @RequestBody Event newEvent)
                                                throws URISyntaxException {
            EventResourceWithLog resource = eventResourceLogMapper.map(eventService.saveNew(newEvent));
            return ResponseEntity.created(new URI(resource.getId().expand().getHref())).body(resource);
    }

    @PutMapping("/{id}")
    @ApiOperation("Modify event.")
    @ApiResponses({
            @ApiResponse(code = 201, message = "Created new resource or modified if set existing id"),
            @ApiResponse(code = 400, message = "Missing not null field; wrong field name; wrong field value; query and body id diverge"),
            @ApiResponse(code = 401, message = "Unauthorized"),
    })
    public ResponseEntity<EventResourceWithLog> update (@Valid @RequestBody Event newEvent,
            @PathVariable Long id) throws URISyntaxException {

        if (newEvent.getId() != null && !newEvent.getId().equals(id)){
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
    @ApiOperation("Delete event by id.")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Event with id x deleted"),
            @ApiResponse(code = 400, message = "Invalid value for id e.g. a letter"),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 404, message = "Event id not found"),
    })
    public ResponseEntity<Resource<SuccessJSON>> delete (@PathVariable Long id){
        eventService.findById(id)
                .orElseThrow(() -> new EventNotFoundException(id));
        eventService.delete(id);

        Resource<SuccessJSON> resource = new Resource<>(new SuccessJSON("Event with id " + id + " removed with success"));
        resource.add(ControllerLinkBuilder.linkTo(EventController.class) .withRel("events"));

        return ResponseEntity.ok(resource);
    }

}
