package br.com.jtech.tasklist.tasklist.services;

import br.com.jtech.tasklist.tasklist.adapters.output.repositories.UserRepository;
import br.com.jtech.tasklist.tasklist.application.core.domains.User;
import br.com.jtech.tasklist.tasklist.application.core.exceptions.UserNotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@SpringBootTest
class UserServiceTest {

    @Autowired
    private UserService userService;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private BCryptPasswordEncoder passwordEncoder;

    @Test
    void deveCadastrarUsuarioQuandoEmailNaoExiste() {

        var user = User.builder()
                .name("Alex")
                .email("alex@email.com")
                .password("123456")
                .build();

        when(userRepository.findByEmail("alex@email.com")).thenReturn(Optional.empty());
        when(passwordEncoder.encode("123456")).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenAnswer(inv -> inv.getArgument(0));

        var result = userService.register(user);

        verify(userRepository).findByEmail("alex@email.com");
        verify(passwordEncoder).encode("123456");

        var savedUser = userRepository.save(user); // mock retorna o mesmo objeto
        assertThat(savedUser.getPassword()).isEqualTo("encodedPassword");
        assertThat(savedUser.getEmail()).isEqualTo("alex@email.com");
        assertThat(result.getName()).isEqualTo("Alex");
    }

    @Test
    void naoDeveCadastrarUsuarioSemNome() {
        var user = User.builder()
                .email("alex@email.com")
                .password("123456")
                .build();

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            userService.register(user);
        });

        assertThat(exception.getMessage()).isEqualTo("Nome é obrigatório");
    }

    @Test
    void naoDeveCadastrarUsuarioSemEmail() {
        var user = User.builder()
                .name("Alex")
                .password("123456")
                .build();

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            userService.register(user);
        });

        assertThat(exception.getMessage()).isEqualTo("Email é obrigatório");
    }

    @Test
    void naoDeveCadastrarUsuarioSemSenha() {
        var user = User.builder()
                .name("Alex")
                .email("alex@email.com")
                .build();

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            userService.register(user);
        });

        assertThat(exception.getMessage()).isEqualTo("Senha é obrigatória");
    }

    @Test
    void deveRetornarUsuarioQuandoIdExistir() {
        UUID userId = UUID.randomUUID();
        var user = User.builder()
                .id(userId)
                .name("Alex")
                .email("alex@email.com")
                .password("123456")
                .build();

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        User result = userService.findById(userId);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(userId);
        assertThat(result.getEmail()).isEqualTo("alex@email.com");

        verify(userRepository).findById(userId);
    }

    @Test
    void deveLancarExcecaoQuandoIdNaoExistir() {
        UUID userId = UUID.randomUUID();

        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        Exception exception = assertThrows(UserNotFoundException.class, () -> {
            userService.findById(userId);
        });

        assertThat(exception.getMessage()).isEqualTo("Usuário não encontrado com id: " + userId);

        verify(userRepository).findById(userId);
    }

    @Test
    void deveAtualizarUsuarioQuandoCamposValidos() {
        UUID userId = UUID.randomUUID();
        var user = User.builder()
                .id(userId)
                .name("Alex")
                .email("alex@email.com")
                .password("123456")
                .build();

        when(passwordEncoder.encode("123456")).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenAnswer(inv -> inv.getArgument(0));

        User result = userService.update(user);

        verify(passwordEncoder).encode("123456");
        verify(userRepository).save(user); // verifica se o save foi chamado apenas 1 vez

        assertThat(result.getPassword()).isEqualTo("encodedPassword");
        assertThat(result.getName()).isEqualTo("Alex");
        assertThat(result.getEmail()).isEqualTo("alex@email.com");
    }

    @Test
    void deveLancarExcecaoQuandoNomeForNulo() {
        var user = User.builder()
                .name(null)
                .email("alex@email.com")
                .password("123456")
                .build();

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            userService.update(user);
        });

        assertThat(exception.getMessage()).isEqualTo("Nome é obrigatório");
    }

    @Test
    void deveLancarExcecaoQuandoEmailForNulo() {
        var user = User.builder()
                .name("Alex")
                .email(null)
                .password("123456")
                .build();

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            userService.update(user);
        });

        assertThat(exception.getMessage()).isEqualTo("Email é obrigatório");
    }

    @Test
    void deveLancarExcecaoQuandoSenhaForNula() {
        var user = User.builder()
                .name("Alex")
                .email("alex@email.com")
                .password(null)
                .build();

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            userService.update(user);
        });

        assertThat(exception.getMessage()).isEqualTo("Senha é obrigatória");
    }

    @Test
    void deveLancarErroQuandoEmailForNulo() {
        String email = null;

        var exception = org.junit.jupiter.api.Assertions.assertThrows(
                IllegalArgumentException.class,
                () -> userService.findByEmail(email)
        );

        assertThat(exception.getMessage()).isEqualTo("Email é obrigatório");
    }

    @Test
    void deveLancarErroQuandoEmailForVazio() {
        String email = " ";

        var exception = org.junit.jupiter.api.Assertions.assertThrows(
                IllegalArgumentException.class,
                () -> userService.findByEmail(email)
        );

        assertThat(exception.getMessage()).isEqualTo("Email é obrigatório");
    }

    @Test
    void deveLancarErroQuandoUsuarioNaoExistir() {
        String email = "inexistente@email.com";
        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());

        var exception = org.junit.jupiter.api.Assertions.assertThrows(
                UserNotFoundException.class,
                () -> userService.findByEmail(email)
        );

        assertThat(exception.getMessage()).isEqualTo("Usuário não encontrado com email: " + email);
    }

    @Test
    void deveRetornarUsuarioQuandoEmailExistir() {
        String email = "alex@email.com";
        var user = User.builder()
                .name("Alex")
                .email(email)
                .password("123456")
                .build();

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));

        User result = userService.findByEmail(email);

        assertThat(result).isNotNull();
        assertThat(result.getEmail()).isEqualTo(email);
        assertThat(result.getName()).isEqualTo("Alex");

        verify(userRepository).findByEmail(email);
    }

    @Test
    void deveLancarErroQuandoDeletarComEmailNulo() {
        String email = null;

        var exception = org.junit.jupiter.api.Assertions.assertThrows(
                IllegalArgumentException.class,
                () -> userService.deleteByEmail(email)
        );

        assertThat(exception.getMessage()).isEqualTo("Email é obrigatório");
    }

    @Test
    void deveLancarErroQuandoDeletarComEmailVazio() {
        String email = " ";

        var exception = org.junit.jupiter.api.Assertions.assertThrows(
                IllegalArgumentException.class,
                () -> userService.deleteByEmail(email)
        );

        assertThat(exception.getMessage()).isEqualTo("Email é obrigatório");
    }

    @Test
    void deveLancarErroQuandoUsuarioNaoExistirParaDeletar() {
        String email = "inexistente@email.com";
        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());

        var exception = org.junit.jupiter.api.Assertions.assertThrows(
                UserNotFoundException.class,
                () -> userService.deleteByEmail(email)
        );

        assertThat(exception.getMessage()).isEqualTo("Usuário não encontrado com email: " + email);
    }

    @Test
    void deveDeletarUsuarioQuandoEmailExistir() {
        String email = "alex@email.com";
        var user = User.builder()
                .name("Alex")
                .email(email)
                .password("123456")
                .build();

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));
        doNothing().when(userRepository).delete(user);

        userService.deleteByEmail(email);

        verify(userRepository).findByEmail(email);
        verify(userRepository).delete(user);
    }

    @Test
    void deveAutenticarUsuarioComEmailESenhaValidos() {
        var user = User.builder()
                .name("Alex")
                .email("alex@email.com")
                .password("encodedPassword")
                .build();

        when(userRepository.findByEmail("alex@email.com")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("123456", "encodedPassword")).thenReturn(true);

        var result = userService.authenticate("alex@email.com", "123456");

        assertThat(result).isNotNull();
        assertThat(result.getEmail()).isEqualTo("alex@email.com");

        verify(userRepository).findByEmail("alex@email.com");
        verify(passwordEncoder).matches("123456", "encodedPassword");
    }

    @Test
    void deveLancarErroQuandoEmailNulo() {
        var exception = org.junit.jupiter.api.Assertions.assertThrows(
                IllegalArgumentException.class,
                () -> userService.authenticate(null, "123456")
        );

        assertThat(exception.getMessage()).isEqualTo("Email é obrigatório");
    }

    @Test
    void deveLancarErroQuandoSenhaNula() {
        var exception = org.junit.jupiter.api.Assertions.assertThrows(
                IllegalArgumentException.class,
                () -> userService.authenticate("alex@email.com", null)
        );

        assertThat(exception.getMessage()).isEqualTo("Senha é obrigatória");
    }

    @Test
    void deveLancarErroQuandoUsuarioNaoExistirCadastrado() {
        when(userRepository.findByEmail("inexistente@email.com")).thenReturn(Optional.empty());

        var exception = org.junit.jupiter.api.Assertions.assertThrows(
                UserNotFoundException.class,
                () -> userService.authenticate("inexistente@email.com", "123456")
        );

        assertThat(exception.getMessage()).isEqualTo("Usuário não encontrado com email: inexistente@email.com");
    }

    @Test
    void deveLancarErroQuandoSenhaInvalida() {
        var user = User.builder()
                .name("Alex")
                .email("alex@email.com")
                .password("encodedPassword")
                .build();

        when(userRepository.findByEmail("alex@email.com")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("wrongPassword", "encodedPassword")).thenReturn(false);

        var exception = org.junit.jupiter.api.Assertions.assertThrows(
                IllegalArgumentException.class,
                () -> userService.authenticate("alex@email.com", "wrongPassword")
        );

        assertThat(exception.getMessage()).isEqualTo("Senha inválida");
    }
}
