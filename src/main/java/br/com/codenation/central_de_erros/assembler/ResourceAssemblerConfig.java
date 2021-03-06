package br.com.codenation.central_de_erros.assembler;

import br.com.codenation.central_de_erros.controller.EventController;
import br.com.codenation.central_de_erros.entity.Event;
import br.com.codenation.central_de_erros.resources.EventResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ResourceAssemblerConfig {

    @Autowired
    protected EventResourceMapper eventResourceMapper;

    @Bean
    public ResourceAssembler<Event, EventResource, EventController> eventResourceAssembler() {
        return new ResourceAssembler<>(EventController.class, EventResource.class,
                eventResourceMapper);
    }
}
