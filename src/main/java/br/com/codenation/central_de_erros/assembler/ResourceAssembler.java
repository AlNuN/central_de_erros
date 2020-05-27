package br.com.codenation.central_de_erros.assembler;

import org.springframework.hateoas.Identifiable;
import org.springframework.hateoas.ResourceSupport;
import org.springframework.hateoas.mvc.IdentifiableResourceAssemblerSupport;

public class ResourceAssembler<E extends Identifiable<Long>, R extends ResourceSupport, C>
extends IdentifiableResourceAssemblerSupport<E,R> {

    private final ResourceMapper<E,R> resourceMapper;

    public ResourceAssembler(Class<C> controllerClass, Class<R> resourceClass, ResourceMapper<E, R> resourceMapper) {
        super(controllerClass, resourceClass);
        this.resourceMapper = resourceMapper;
    }

    @Override
    public R toResource(E entity){
        return resourceMapper.map(entity);
    }
}
