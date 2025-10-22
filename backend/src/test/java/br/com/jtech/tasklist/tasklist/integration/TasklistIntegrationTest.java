package br.com.jtech.tasklist.tasklist.integration;

import br.com.jtech.tasklist.tasklist.TasklistApplication;
import br.com.jtech.tasklist.tasklist.adapters.output.repositories.UserRepository;
import br.com.jtech.tasklist.tasklist.adapters.output.repositories.entities.TasklistEntity;
import br.com.jtech.tasklist.tasklist.application.core.domains.User;
import br.com.jtech.tasklist.tasklist.services.TasklistService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = TasklistApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Import(TestSecurityConfig.class)
public class TasklistIntegrationTest {

    @Autowired
    private TasklistService tasklistService;

    @Autowired
    private UserRepository userRepository;

    @Test
    void shouldCreateTaskWithExistingUser() {

        User user = createTestUser();

        TasklistEntity task = TasklistEntity.builder()
                .title("Minha Tarefa")
                .listName("Lista Teste")
                .user(user)
                .build();

        TasklistEntity savedTask = tasklistService.create(task);

        assertNotNull(savedTask.getId());
        assertEquals(user.getId(), savedTask.getUser().getId());
        assertEquals("Minha Tarefa", savedTask.getTitle());
    }

    @Test
    void shouldUpdateTask() {
        User user = createTestUser();

        TasklistEntity task = TasklistEntity.builder()
                .title("Tarefa Original")
                .listName("Lista Original")
                .user(user)
                .build();

        TasklistEntity savedTask = tasklistService.create(task);

        savedTask.setTitle("Tarefa Atualizada");
        savedTask.setListName("Lista Atualizada");

        TasklistEntity updatedTask = tasklistService.update(savedTask);

        assertEquals("Tarefa Atualizada", updatedTask.getTitle());
        assertEquals("Lista Atualizada", updatedTask.getListName());
    }

    @Test
    void shouldGetTaskById() {
        User user = createTestUser();

        TasklistEntity task = TasklistEntity.builder()
                .title("Tarefa para Buscar")
                .listName("Lista Buscar")
                .user(user)
                .build();

        TasklistEntity savedTask = tasklistService.create(task);

        TasklistEntity fetchedTask = tasklistService.findById(savedTask.getId());

        assertEquals(savedTask.getId(), fetchedTask.getId());
        assertEquals(savedTask.getTitle(), fetchedTask.getTitle());
    }

    @Test
    void shouldGetTasksByUser() {
        User user = createTestUser();

        TasklistEntity task1 = TasklistEntity.builder()
                .title("Tarefa 1")
                .listName("Lista 1")
                .user(user)
                .build();

        TasklistEntity task2 = TasklistEntity.builder()
                .title("Tarefa 2")
                .listName("Lista 2")
                .user(user)
                .build();

        tasklistService.create(task1);
        tasklistService.create(task2);

        List<TasklistEntity> tasks = tasklistService.findByUser(user);

        assertEquals(2, tasks.size());
    }

    @Test
    void shouldDeleteTask() {
        User user = createTestUser();

        TasklistEntity task = TasklistEntity.builder()
                .title("Tarefa para Deletar")
                .listName("Lista Deletar")
                .user(user)
                .build();

        TasklistEntity savedTask = tasklistService.create(task);

        tasklistService.delete(savedTask.getId());

        assertThrows(Exception.class, () -> tasklistService.findById(savedTask.getId()));
    }

    private User createTestUser() {
        User user = User.builder()
                .name("Test User")
                .email("user+" + System.currentTimeMillis() + "@test.com")
                .password("1234")
                .build();
        return userRepository.save(user);
    }
}
