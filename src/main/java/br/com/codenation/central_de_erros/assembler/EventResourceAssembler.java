package br.com.codenation.central_de_erros.assembler;

import br.com.codenation.central_de_erros.controller.EventController;
import br.com.codenation.central_de_erros.entity.Event;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.PagedResources;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.ResourceAssembler;
import org.springframework.stereotype.Component;

import java.awt.print.Pageable;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.*;

@Component
public class EventResourceAssembler implements ResourceAssembler<Event, Resource<Event>> {

    @Override
    public Resource<Event> toResource(Event event) {
        return new Resource<>(event, linkTo(methodOn(EventController.class).one(event.getId())).withSelfRel());
//                linkTo(methodOn(EventController.class).all()).withRel("events")
    }
}
