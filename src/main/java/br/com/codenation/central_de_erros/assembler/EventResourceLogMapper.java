package br.com.codenation.central_de_erros.assembler;

import br.com.codenation.central_de_erros.controller.EventController;
import br.com.codenation.central_de_erros.entity.Event;
import br.com.codenation.central_de_erros.resources.EventResourceWithLog;
import org.mapstruct.*;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.mvc.ControllerLinkBuilder;

@Mapper(componentModel = "spring")
public interface EventResourceLogMapper extends ResourceMapper<Event, EventResourceWithLog>{

    @Mapping(source= "id", target = "eventId")
    @Mapping(source="dateTime", target= "dateTime", dateFormat= "yyyy-MM-dd HH:mm")
    EventResourceWithLog map(Event event);

    @AfterMapping
    default void addLinks(@MappingTarget EventResourceWithLog resource, Event entity){
        Link selfLink = ControllerLinkBuilder.linkTo(EventController.class)
                .slash(entity)
                .withSelfRel();
        Link allLink = ControllerLinkBuilder.linkTo(EventController.class)
                .withRel("events");
        resource.add(selfLink, allLink);
    }
}
