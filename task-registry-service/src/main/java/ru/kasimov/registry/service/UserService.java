package ru.kasimov.registry.service;

import org.springframework.security.core.userdetails.UserDetailsService;
import ru.kasimov.registry.domain.model.User;

import java.util.Optional;

public interface UserService {

    User saveUser(User user);

    User createTask(User user);

    User getByUsername(String username);

    User getByEmail(String email);

    UserDetailsService userDetailsService();

    User getCurrentUser();

}
