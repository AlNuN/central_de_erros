package br.com.codenation.central_de_erros.assembler;

import br.com.codenation.central_de_erros.controller.EventController;
import br.com.codenation.central_de_erros.controller.UserController;
import br.com.codenation.central_de_erros.entity.User;
import br.com.codenation.central_de_erros.resources.UserResource;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.mvc.ControllerLinkBuilder;

@Mapper(componentModel = "spring")
public interface UserResourceMapper extends ResourceMapper<User, UserResource> {

    @Mapping(source = "id", target = "userId")
    UserResource map(User user);

    @AfterMapping
    default void addLinks(@MappingTarget UserResource resource, User entity){
        Link selfLink = ControllerLinkBuilder.linkTo(UserController.class)
                .slash(entity.getId())
                .withSelfRel();
        Link allLink = ControllerLinkBuilder.linkTo(UserController.class)
                .withRel("users");
        resource.add(selfLink, allLink);
    }
}
