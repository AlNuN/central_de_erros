package br.com.codenation.central_de_erros.repository;

import br.com.codenation.central_de_erros.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
}
