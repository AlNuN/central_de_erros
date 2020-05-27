package br.com.codenation.central_de_erros.assembler;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@FunctionalInterface
public interface ResourceMapper<E,R> {
    R map(E entity);

    default List<R> map(Collection<E> entities) {
        return entities.stream()
                .map(this::map)
                .collect(Collectors.toList());
    }
}
