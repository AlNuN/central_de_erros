package br.com.codenation.central_de_erros.service;

import br.com.codenation.central_de_erros.entity.User;
import br.com.codenation.central_de_erros.repository.UserRepository;
import br.com.codenation.central_de_erros.service.interfaces.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository repository;

    @Override
    public Optional<User> findById(Long id) {
        return repository.findById(id);
    }

    @Override
    public User save(User object) {
        return repository.save(object);
    }
}
