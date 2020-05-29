package br.com.codenation.central_de_erros.assembler;

import br.com.codenation.central_de_erros.controller.EventController;
import br.com.codenation.central_de_erros.entity.Event;
import br.com.codenation.central_de_erros.resources.EventResource;
import org.mapstruct.*;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.mvc.ControllerLinkBuilder;

@Mapper(componentModel = "spring")
public interface EventResourceMapper extends ResourceMapper<Event, EventResource> {

    @Mapping(source= "id", target = "eventId")
    @Mapping(source="dateTime", target= "dateTime", dateFormat= "yyyy-MM-dd HH:mm")
    EventResource map(Event event);

    @AfterMapping
    default void addLinks(@MappingTarget EventResource resource, Event entity){
        Link selfLink = ControllerLinkBuilder.linkTo(EventController.class)
                .slash(entity)
                .withSelfRel();
        Link allLink = ControllerLinkBuilder.linkTo(EventController.class)
                .withRel("events");
        resource.add(selfLink, allLink);
    }
}
