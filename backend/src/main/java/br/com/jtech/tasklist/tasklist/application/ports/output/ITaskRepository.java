package br.com.jtech.tasklist.tasklist.application.ports.output;

import br.com.jtech.tasklist.tasklist.adapters.output.repositories.entities.TasklistEntity;
import br.com.jtech.tasklist.tasklist.application.core.domains.User;

import java.util.List;

public interface ITaskRepository {

    List<TasklistEntity> findByUser(User user);
}
