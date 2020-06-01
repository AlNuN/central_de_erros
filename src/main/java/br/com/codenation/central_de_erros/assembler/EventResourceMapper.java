package br.com.codenation.central_de_erros.assembler;

import br.com.codenation.central_de_erros.entity.Event;
import br.com.codenation.central_de_erros.resources.EventResource;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface EventResourceMapper extends ResourceMapper<Event, EventResource>,
ResourceBuilder<Event, EventResource>{

    @Mapping(source= "id", target = "eventId")
    @Mapping(source="dateTime", target= "dateTime", dateFormat= "yyyy-MM-dd HH:mm")
    EventResource map(Event event);

}
