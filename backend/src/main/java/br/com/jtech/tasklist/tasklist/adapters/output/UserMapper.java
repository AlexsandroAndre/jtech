package br.com.jtech.tasklist.tasklist.adapters.output;

import br.com.jtech.tasklist.tasklist.adapters.input.protocols.user.UserRequest;
import br.com.jtech.tasklist.tasklist.adapters.input.protocols.user.UserResponse;
import br.com.jtech.tasklist.tasklist.application.core.domains.User;

public class UserMapper {

    public static User toEntity(UserRequest request) {
        if (request == null) return null;


        return User.builder()
                .id(request.getId())
                .email(request.getEmail())
                .name(request.getName())
                .password(request.getPassword())
                .build();
    }

    public static UserRequest toRequest(User entity) {
        if (entity == null) return null;

        return UserRequest.builder()
                .id(entity.getId())
                .email(entity.getEmail())
                .name(entity.getName())
                .password(entity.getPassword())
                .build();
    }

    public static UserResponse toResponse(User entity){
        if (entity == null) return null;

        return UserResponse.builder()
                .id(entity.getId())
                .email(entity.getEmail())
                .name(entity.getName())
                .password(entity.getPassword())
                .build();
    }
}
