package br.com.codenation.central_de_erros.repository;

import br.com.codenation.central_de_erros.entity.Event;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EventRepository extends JpaRepository<Event, Long> {
}
