package br.com.codenation.central_de_erros.service.interfaces;

import br.com.codenation.central_de_erros.entity.User;

import java.util.Optional;

public interface UserService extends ServiceInterface<User> {
    Optional<User> findById(Long id);
}
