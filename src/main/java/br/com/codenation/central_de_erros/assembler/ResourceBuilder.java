package br.com.codenation.central_de_erros.assembler;

import br.com.codenation.central_de_erros.controller.EventController;
import org.mapstruct.AfterMapping;
import org.mapstruct.MappingTarget;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.ResourceSupport;
import org.springframework.hateoas.mvc.ControllerLinkBuilder;

public interface ResourceBuilder<E,R extends ResourceSupport> {

    @AfterMapping
    default void addLinks(@MappingTarget R resource, E entity){
        Link selfLink = ControllerLinkBuilder.linkTo(EventController.class)
                .slash(entity)
                .withSelfRel();
        Link allLink = ControllerLinkBuilder.linkTo(EventController.class)
                .withRel("events");
        resource.add(selfLink, allLink);
    }

}
