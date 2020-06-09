package br.com.codenation.central_de_erros.resources;


import br.com.codenation.central_de_erros.entity.Level;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.hateoas.ResourceSupport;
import org.springframework.hateoas.core.Relation;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Relation(value = "event", collectionRelation = "events")
public class EventResourceWithLog extends ResourceSupport {

    @JsonProperty("id")
    public Long eventId;
    public Level level;
    public String description;
    public String origin;
    public String dateTime;
    public Long number;
    public String log;

}
