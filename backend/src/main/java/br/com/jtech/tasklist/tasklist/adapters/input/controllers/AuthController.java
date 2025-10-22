package br.com.jtech.tasklist.tasklist.adapters.input.controllers;

import br.com.jtech.tasklist.tasklist.application.core.domains.User;
import br.com.jtech.tasklist.tasklist.config.security.JwtService;
import br.com.jtech.tasklist.tasklist.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;
    private final JwtService jwtService;

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestParam String email, @RequestParam String password) {
        User user = userService.authenticate(email, password);
        String token = jwtService.generateToken(user.getEmail());
        return ResponseEntity.ok(token);
    }
}
