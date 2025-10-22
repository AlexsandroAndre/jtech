package br.com.jtech.tasklist.tasklist.application.ports.input;

import br.com.jtech.tasklist.tasklist.application.core.domains.User;

import java.util.UUID;

public interface IUserService {
    User register(User user);
    User update(User user);
    void deleteByEmail(String email);
    User findById(UUID id);
    User findByEmail(String email);
    User authenticate(String email, String password);
}
