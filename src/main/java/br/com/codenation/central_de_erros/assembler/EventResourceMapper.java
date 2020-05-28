package br.com.codenation.central_de_erros.assembler;

import br.com.codenation.central_de_erros.controller.EventController;
import br.com.codenation.central_de_erros.entity.Event;
import br.com.codenation.central_de_erros.resources.EventResource;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.mvc.ControllerLinkBuilder;

@Mapper
public interface EventResourceMapper extends ResourceMapper<Event, EventResource> {

    EventResourceMapper INSTANCE = Mappers.getMapper(EventResourceMapper.class);

    @Mappings({
            @Mapping(source="id", target= "eventId"),
            @Mapping(source="level", target= "level"),
            @Mapping(source="description", target= "description"),
            @Mapping(source="origin", target= "origin"),
            @Mapping(source="dateTime", target= "dateTime", dateFormat= "yyyy-MM-dd HH:mm"),
            @Mapping(source="repeated", target= "repeated")
    })
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
