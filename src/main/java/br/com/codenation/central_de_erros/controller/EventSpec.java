package br.com.codenation.central_de_erros.controller;

import br.com.codenation.central_de_erros.entity.Event;
import net.kaczmarzyk.spring.data.jpa.domain.*;
import net.kaczmarzyk.spring.data.jpa.web.annotation.And;
import net.kaczmarzyk.spring.data.jpa.web.annotation.OnTypeMismatch;
import net.kaczmarzyk.spring.data.jpa.web.annotation.Spec;
import org.springframework.data.jpa.domain.Specification;

@And({
        @Spec(path = "level", spec = Equal.class, onTypeMismatch = OnTypeMismatch.EXCEPTION),
        @Spec(path = "description", spec = Like.class),
        @Spec(path = "origin", spec = Like.class),
        @Spec(path = "log", spec = Like.class),
        @Spec(path = "dateTime", spec = Equal.class, config = "yyyy-MM-dd HH:mm"),
        @Spec(path = "dateTime", params = {"dateTimeAfter", "dateTimeBefore"}, config = "yyyy-MM-dd HH:mm",
                spec = Between.class),
        @Spec(path = "dateTime", params = "dateTimePre", spec = LessThan.class, config = "yyyy-MM-dd HH:mm"),
        @Spec(path = "dateTime", params = "dateTimePos", spec = GreaterThan.class, config = "yyyy-MM-dd HH:mm"),
        @Spec(path = "number", spec = Equal.class, onTypeMismatch = OnTypeMismatch.EXCEPTION)
})
public interface EventSpec extends Specification<Event> {
}
