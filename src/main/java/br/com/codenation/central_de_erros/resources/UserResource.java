package br.com.codenation.central_de_erros.resources;

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
@Relation(value = "user", collectionRelation = "users")
public class UserResource extends ResourceSupport {
    @JsonProperty("id")
    public Long userId;
    public String email;
    public String password;
}
