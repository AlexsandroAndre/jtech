package br.com.jtech.tasklist.tasklist.application.ports.input;

import br.com.jtech.tasklist.tasklist.adapters.output.repositories.entities.TasklistEntity;
import br.com.jtech.tasklist.tasklist.adapters.output.repositories.entities.UserEntity;
import br.com.jtech.tasklist.tasklist.application.core.domains.User;

import java.util.List;
import java.util.UUID;

public interface ITaskService {
    TasklistEntity create(TasklistEntity task);
    TasklistEntity update(TasklistEntity task);
    TasklistEntity findById(UUID id);
    List<TasklistEntity> findByUser(User user);
    void delete(UUID id);
}
