package br.com.codenation.central_de_erros.service.Impl;

import br.com.codenation.central_de_erros.entity.User;
import br.com.codenation.central_de_erros.exception.WrongUserInputException;
import br.com.codenation.central_de_erros.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    private UserRepository repository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return repository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Email " + email + " not found."));
    }

    public User getByEmail(String email){
        return repository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Email " + email + " not found."));
    }

    public User save(User user){
        if(user.getId() != null) { throw new WrongUserInputException("You should not provide id."); }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return repository.save(user);
    }

    public User update(User user, Authentication auth){
        if(user.getId() != null) { throw new WrongUserInputException("You should not provide id."); }
        User oldUser = repository.findByEmail(auth.getName())
                .orElseThrow(() -> new UsernameNotFoundException("Email " + auth.getName() + " not found."));
        if (user.getEmail() != null){ oldUser.setEmail(user.getEmail()); }
        if (user.getPassword() != null){ oldUser.setPassword(passwordEncoder.encode(user.getPassword())); }
        return repository.save(oldUser);
    }
}
