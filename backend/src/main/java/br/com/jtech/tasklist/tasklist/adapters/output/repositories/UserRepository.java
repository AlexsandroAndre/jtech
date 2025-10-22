package br.com.jtech.tasklist.tasklist.adapters.output.repositories;

import br.com.jtech.tasklist.tasklist.application.core.domains.User;
import br.com.jtech.tasklist.tasklist.application.ports.output.IUserRepository;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID>, IUserRepository {

    Optional<User> findByEmail(String email);

}
