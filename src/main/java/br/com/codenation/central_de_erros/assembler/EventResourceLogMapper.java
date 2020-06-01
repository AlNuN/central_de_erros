package br.com.codenation.central_de_erros.assembler;

import br.com.codenation.central_de_erros.entity.Event;
import br.com.codenation.central_de_erros.resources.EventResourceWithLog;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface EventResourceLogMapper extends ResourceMapper<Event, EventResourceWithLog>,
ResourceBuilder<Event, EventResourceWithLog>{

    @Mapping(source= "id", target = "eventId")
    @Mapping(source="dateTime", target= "dateTime", dateFormat= "yyyy-MM-dd HH:mm")
    EventResourceWithLog map(Event event);

}
