package br.com.codenation.central_de_erros.assembler;

import br.com.codenation.central_de_erros.entity.Event;
import org.mapstruct.*;

@Mapper(componentModel = "spring",
nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface EventMapper {

    Event map(@MappingTarget Event event, Event newEvent);
}
