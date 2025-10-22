package br.com.jtech.tasklist.tasklist.adapters.output.repositories;

import br.com.jtech.tasklist.tasklist.adapters.output.repositories.entities.TasklistEntity;
import br.com.jtech.tasklist.tasklist.application.core.domains.User;
import br.com.jtech.tasklist.tasklist.application.ports.output.ITaskRepository;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface TasklistRepository extends JpaRepository<TasklistEntity, UUID>, ITaskRepository {
    List<TasklistEntity> findByUser(User user);
}