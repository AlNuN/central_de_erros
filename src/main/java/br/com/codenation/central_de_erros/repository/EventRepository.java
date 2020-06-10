package br.com.codenation.central_de_erros.repository;

import br.com.codenation.central_de_erros.entity.Event;
import br.com.codenation.central_de_erros.entity.Level;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;


public interface EventRepository extends JpaRepository<Event, Long>,
        JpaSpecificationExecutor<Event> {

    Page<Event> findAll(Specification<Event> specs, Pageable pageable);
    Optional<Event> findByLevelAndDescriptionAndLogAndOrigin(Level level,
                                                              String description,
                                                              String log,
                                                              String Origin);
}
