package br.com.jtech.tasklist.tasklist.services;

import br.com.jtech.tasklist.tasklist.adapters.output.repositories.TasklistRepository;
import br.com.jtech.tasklist.tasklist.adapters.output.repositories.UserRepository;
import br.com.jtech.tasklist.tasklist.application.core.domains.User;
import br.com.jtech.tasklist.tasklist.application.core.exceptions.UserNotFoundException;
import br.com.jtech.tasklist.tasklist.application.ports.input.IUserService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

import static br.com.jtech.tasklist.tasklist.application.core.constants.Messages.*;

@Service
@RequiredArgsConstructor
public class UserService implements IUserService {

    private final UserRepository userRepository;
    private final TasklistRepository taskRepository;
    private final BCryptPasswordEncoder passwordEncoder;


    public User register(User user) {

        if (user.getName() == null || user.getName().isBlank()) {
            throw new IllegalArgumentException(NOME_OBRIGATORIO);
        }
        if (user.getEmail() == null || user.getEmail().isBlank()) {
            throw new IllegalArgumentException(EMAIL_OBRIGATORIO);
        }
        if (user.getPassword() == null || user.getPassword().isBlank()) {
            throw new IllegalArgumentException(SENHA_OBRIGATORIA);
        }

        Optional<User> existingUser = userRepository.findByEmail(user.getEmail());
        if (existingUser.isPresent()) {
            throw new IllegalArgumentException(EMAIL_JA_CADASTRADO);
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));

        return userRepository.save(user);
    }

    public User findById(UUID id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(String.format(USUARIO_NAO_ENCONTRADO_ID, id)));
    }

    public User update(User user) {

        if (user.getName() == null || user.getName().isBlank()) {
            throw new IllegalArgumentException(NOME_OBRIGATORIO);
        }

        if (user.getEmail() == null || user.getEmail().isBlank()) {
            throw new IllegalArgumentException(EMAIL_OBRIGATORIO);
        }

        if (user.getPassword() == null || user.getPassword().isBlank()) {
            throw new IllegalArgumentException(SENHA_OBRIGATORIA);
        }

        User existing = findById(user.getId());

        existing.setName(user.getName());
        existing.setEmail(user.getEmail());
        existing.setPassword(passwordEncoder.encode(user.getPassword()));

        return userRepository.save(existing);
    }

    public void deleteByEmail(String email) {

        if(email == null || email.isBlank()){
            throw new IllegalArgumentException(EMAIL_OBRIGATORIO);
        }

        User user = findByEmail(email);

        userRepository.delete(user);
    }

    public User findByEmail(String email) {

        if(email == null || email.isBlank()){
            throw new IllegalArgumentException(EMAIL_OBRIGATORIO);
        }

            return userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException(String.format(USUARIO_NAO_ENCONTRADO, email)));
    }

    @Transactional
    public void delete(UUID id) {
        User user = findById(id);

        userRepository.delete(user);
    }

    public User authenticate(String email, String password) {
        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException(EMAIL_OBRIGATORIO);
        }
        if (password == null || password.isBlank()) {
            throw new IllegalArgumentException(SENHA_OBRIGATORIA);
        }

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException(String.format(USUARIO_NAO_ENCONTRADO, email)));

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new IllegalArgumentException(SENHA_INVALIDA);
        }

        return user;
    }
}
