package br.com.codenation.central_de_erros.resources;

import br.com.codenation.central_de_erros.entity.ErrorTypes;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.hateoas.ResourceSupport;
import org.springframework.hateoas.core.Relation;

import java.time.LocalDateTime;

@Relation(value = "event", collectionRelation = "events")
public class EventResource extends ResourceSupport {

    @JsonProperty("id")
    public Long eventId;
    public ErrorTypes errorType;
    public String description;
    public String origin;
    public LocalDateTime dateTime;
    public Long repeated;

}
