package br.com.jtech.tasklist.tasklist.adapters.input.controllers;

import br.com.jtech.tasklist.tasklist.adapters.input.protocols.task.TasklistRequest;
import br.com.jtech.tasklist.tasklist.adapters.output.TasklistMapper;
import br.com.jtech.tasklist.tasklist.adapters.output.repositories.entities.TasklistEntity;
import br.com.jtech.tasklist.tasklist.application.core.domains.User;
import br.com.jtech.tasklist.tasklist.services.TasklistService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/tasks")
@RequiredArgsConstructor
@Tag(name = "Tarefas", description = "Operações relacionadas a tarefas")
public class TasklistController {

    private final TasklistService taskService;

    @PostMapping
    @Operation(summary = "Responsável por criar tarefa")
    public ResponseEntity<TasklistEntity> createTask(@RequestBody TasklistRequest task) {

        return ResponseEntity.ok(taskService.create(TasklistMapper.toEntity(task)));
    }

    @PutMapping
    @Operation(summary = "Responsável por atualizar tarefa")
    public ResponseEntity<TasklistEntity> updateTask(@RequestBody TasklistRequest task) {
        return ResponseEntity.ok(taskService.update(TasklistMapper.toEntity(task)));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Responsável por retornar os dados da tarefa")
    public ResponseEntity<TasklistEntity> getTaskById(@PathVariable UUID id) {
        return ResponseEntity.ok(taskService.findById(id));
    }

    @GetMapping("/user/{userId}")
    @Operation(summary = "Responsável por retornar as tarefas do usuário")
    public ResponseEntity<List<TasklistEntity>> getTasksByUser(@PathVariable UUID userId) {
        User user = User.builder().id(userId).build();
        user.setId(userId);
        return ResponseEntity.ok(taskService.findByUser(user));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Responsável por excluir tarefa")
    public ResponseEntity<Void> deleteTask(@PathVariable UUID id) {
        taskService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
