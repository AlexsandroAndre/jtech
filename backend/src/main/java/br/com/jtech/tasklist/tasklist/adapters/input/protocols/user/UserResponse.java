package br.com.jtech.tasklist.tasklist.adapters.input.protocols.user;

import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class UserResponse {

    private UUID id;
    private String name;
    private String email;
    private String password;
}
