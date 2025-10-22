package br.com.jtech.tasklist.tasklist.adapters.input.protocols.task;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TasklistRequest {

    @Schema(description = "Identificador único do usuário (opcional para criação).", example = "174d1b71-4c67-4253-b292-fad6db37a899")
    private UUID id;

    @Schema(description = "O título da tarefa.", example = "Finalizar a implementação do TDD", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "O título da tarefa é obrigatório.")
    @Size(max = 100, message = "O título deve ter no máximo 100 caracteres.")
    private String title;

    @Schema(description = "Descrição detalhada da tarefa.", example = "Garantir que os testes de validação da Request passem.")
    @Size(max = 500, message = "A descrição deve ter no máximo 500 caracteres.")
    private String description;

    @Schema(description = "O nome da lista de tarefas à qual esta tarefa pertence.", example = "Backlog da Sprint 1", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "O nome da lista (listName) é obrigatório.")
    private String listName;

    @Schema(description = "Referência do usuário.", example = "174d1b71-4c67-4253-b292-fad6db37a899", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "ID do usuário é obrigatório.")
    private UUID userId;

    @Schema(description = "Status de conclusão da tarefa.", example = "false")
    private boolean completed;
}
