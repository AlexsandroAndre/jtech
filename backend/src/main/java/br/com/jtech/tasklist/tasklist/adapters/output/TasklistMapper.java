package br.com.jtech.tasklist.tasklist.adapters.output;

import br.com.jtech.tasklist.tasklist.adapters.input.protocols.task.TasklistRequest;
import br.com.jtech.tasklist.tasklist.adapters.output.repositories.entities.TasklistEntity;
import br.com.jtech.tasklist.tasklist.application.core.domains.User;

public class TasklistMapper {

    public static TasklistEntity toEntity(TasklistRequest request) {
        if (request == null) return null;

        TasklistEntity task = TasklistEntity.builder()
                                .id(request.getId())
                                .title(request.getTitle())
                                .description(request.getDescription())
                                .listName(request.getListName())
                                .completed(request.isCompleted())
                                .build();


        User user = User.builder()
                        .id(request.getUserId())
                        .build();

        task.setUser(user);

        return task;
    }

    public static TasklistRequest toRequest(TasklistEntity entity) {
        if (entity == null) return null;

        return TasklistRequest.builder()
                .id(entity.getId())
                .title(entity.getTitle())
                .completed(entity.isCompleted())
                .listName(entity.getListName())
                .userId(entity.getUser().getId())
                .build();
    }
}
