package br.com.jtech.tasklist.tasklist.adapters.input.controllers;

import br.com.jtech.tasklist.tasklist.adapters.input.protocols.user.UserRequest;
import br.com.jtech.tasklist.tasklist.adapters.input.protocols.user.UserResponse;
import br.com.jtech.tasklist.tasklist.adapters.output.UserMapper;
import br.com.jtech.tasklist.tasklist.application.core.domains.User;
import br.com.jtech.tasklist.tasklist.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@Tag(name = "Usuários", description = "Operações relacionadas a usuários")
public class UserController {

    private static final Logger LOGGER = Logger.getLogger(UserController.class.getName());
    private final UserService userService;

    @PostMapping
    @Operation(summary = "Responsável por criar usuário")
    public ResponseEntity<User> createUser(@RequestBody UserRequest userRequest) {
        User saved = userService.register(UserMapper.toEntity(userRequest));
        return ResponseEntity.ok(saved);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Responsável por retornar os dados do usuário")
    public ResponseEntity<UserResponse> getUserById(@PathVariable UUID id) {
        User user = userService.findById(id);
        return ResponseEntity.ok(UserMapper.toResponse(user));
    }

    @GetMapping("/email/{email:.+}")
    @Operation(summary = "Responsável por retornar os dados do usuário por email")
    public ResponseEntity<UserResponse> getByEmail(@PathVariable String email) {
        User user = userService.findByEmail(email);
        return ResponseEntity.ok(UserMapper.toResponse(user));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Responsável por atualizar os dados do usuário")
    public ResponseEntity<UserResponse> updateUser(@PathVariable UUID id, @RequestBody UserRequest userRequest) {
        userRequest.setId(id);
        User updated = userService.update(UserMapper.toEntity(userRequest));
        return ResponseEntity.ok(UserMapper.toResponse(updated));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Responsável por excluir o usuário")
    public ResponseEntity<Void> deleteUser(@PathVariable UUID id) {
        try{
            userService.delete(id);
            return ResponseEntity.noContent().build();
        }catch (Exception e){
            LOGGER.log(Level.SEVERE, "Erro ao deletar usuário: " + id, e);
            throw e;
        }
    }
}
