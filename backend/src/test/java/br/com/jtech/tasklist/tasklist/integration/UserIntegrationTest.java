package br.com.jtech.tasklist.tasklist.integration;

import br.com.jtech.tasklist.tasklist.TasklistApplication;
import br.com.jtech.tasklist.tasklist.adapters.input.protocols.user.UserRequest;
import br.com.jtech.tasklist.tasklist.adapters.input.protocols.user.UserResponse;
import br.com.jtech.tasklist.tasklist.adapters.output.repositories.UserRepository;
import br.com.jtech.tasklist.tasklist.application.core.domains.User;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = TasklistApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Import(TestSecurityConfig.class)
public class UserIntegrationTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private UserRepository userRepository;

    private final static String BASE_URL = "/api/users";

    @BeforeEach
    void setup() {
        userRepository.deleteAll();
    }

    @Test
    void shouldCreateUser() {
        UserRequest user = UserRequest.builder()
                .name("Test User")
                .email("user+" + System.currentTimeMillis() + "@test.com")
                .password("1234")
                .build();

        ResponseEntity<User> response = restTemplate.postForEntity(BASE_URL, user, User.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody().getId());
        assertEquals(user.getEmail(), response.getBody().getEmail());
    }

    @Test
    void shouldGetUserById() {
        User saved = userRepository.save(User.builder()
                .name("Test User")
                .email("user+" + System.currentTimeMillis() + "@test.com")
                .password("1234")
                .build());

        ResponseEntity<UserResponse> response = restTemplate.getForEntity(BASE_URL + "/" + saved.getId(), UserResponse.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(saved.getEmail(), response.getBody().getEmail());
    }

    @Test
    void shouldUpdateUser() {
        User saved = userRepository.save(User.builder()
                .name("Test User")
                .email("user+" + System.currentTimeMillis() + "@test.com")
                .password("1234")
                .build());

        saved.setName("Updated Name");
        HttpEntity<User> request = new HttpEntity<>(saved);

        ResponseEntity<UserResponse> response = restTemplate.exchange(BASE_URL + "/" + saved.getId(),
                HttpMethod.PUT, request, UserResponse.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Updated Name", response.getBody().getName());
    }
}
