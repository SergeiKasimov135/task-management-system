package ru.kasimov.registry.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.kasimov.registry.domain.model.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsername(String username);

    Optional<User> findByEmail(String email);

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);

}
