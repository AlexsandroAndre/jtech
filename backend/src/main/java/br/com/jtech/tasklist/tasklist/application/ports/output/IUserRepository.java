package br.com.jtech.tasklist.tasklist.application.ports.output;

import br.com.jtech.tasklist.tasklist.application.core.domains.User;

import java.util.Optional;

public interface IUserRepository {
    Optional<User> findByEmail(String email);
}
