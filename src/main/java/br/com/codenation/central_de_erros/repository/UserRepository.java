package br.com.codenation.central_de_erros.repository;

import br.com.codenation.central_de_erros.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User,Long> {
}
