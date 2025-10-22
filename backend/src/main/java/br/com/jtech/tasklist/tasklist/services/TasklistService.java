package br.com.jtech.tasklist.tasklist.services;

import br.com.jtech.tasklist.tasklist.adapters.output.repositories.TasklistRepository;
import br.com.jtech.tasklist.tasklist.adapters.output.repositories.UserRepository;
import br.com.jtech.tasklist.tasklist.adapters.output.repositories.entities.TasklistEntity;
import br.com.jtech.tasklist.tasklist.adapters.output.repositories.entities.UserEntity;
import br.com.jtech.tasklist.tasklist.application.core.domains.User;
import br.com.jtech.tasklist.tasklist.application.core.exceptions.TaskNotFoundException;
import br.com.jtech.tasklist.tasklist.application.ports.input.ITaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static br.com.jtech.tasklist.tasklist.application.core.constants.Messages.*;

@Service
@RequiredArgsConstructor
public class TasklistService implements ITaskService {

    private final TasklistRepository taskRepository;
    private final UserRepository userRepository;

    @Override
    public TasklistEntity create(TasklistEntity task) {
        validateTask(task);

        User managedUser = findUserById(task.getUser().getId());

        task.setUser(managedUser);

        task.setCreatedAt(LocalDateTime.now());


        return taskRepository.save(task);
    }

    @Override
    public TasklistEntity update(TasklistEntity task) {
        if(task.getId() == null) {
            throw new IllegalArgumentException(ID_OBRIGATORIO);
        }

        validateTask(task);

        TasklistEntity existing = findById(task.getId());

        task.setId(existing.getId());
        task.setTitle(existing.getTitle());
        task.setUser(existing.getUser());
        task.setUpdatedAt(LocalDateTime.now());
        task.setCreatedAt(existing.getCreatedAt());
        task.setCompleted(existing.isCompleted());

        return taskRepository.save(task);
    }

    @Override
    public TasklistEntity findById(UUID id) {
        return taskRepository.findById(id)
                .orElseThrow(() -> new TaskNotFoundException(String.format(TAREFA_NAO_ENCONTRADO_ID, id)));
    }

    @Override
    public List<TasklistEntity> findByUser(User user) {
        if (user == null) {
            throw new IllegalArgumentException(USUARIO_OBRIGATORIO);
        }
        return taskRepository.findByUser(user);
    }

    @Override
    public void delete(UUID id) {
        TasklistEntity task = findById(id);
        taskRepository.delete(task);
    }

    private void validateTask(TasklistEntity task) {
        if (!StringUtils.hasText(task.getTitle())) {
            throw new IllegalArgumentException(TITULO_OBRIGATORIO);
        }
        if (!StringUtils.hasText(task.getListName())) {
            throw new IllegalArgumentException(NOME_TAREFA_LISTA_OBRIGATORIO);
        }
        if (task.getUser() == null) {
            throw new IllegalArgumentException(USUARIO_OBRIGATORIO);
        }
    }

    private User findUserById(UUID id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado"));
    }
}
