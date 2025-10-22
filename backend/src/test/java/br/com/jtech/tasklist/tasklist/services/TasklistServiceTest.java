package br.com.jtech.tasklist.tasklist.services;

import br.com.jtech.tasklist.tasklist.adapters.output.repositories.TasklistRepository;
import br.com.jtech.tasklist.tasklist.adapters.output.repositories.entities.TasklistEntity;
import br.com.jtech.tasklist.tasklist.application.core.domains.User;
import br.com.jtech.tasklist.tasklist.application.core.exceptions.TaskNotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
public class TasklistServiceTest {

    @Autowired
    private TasklistService taskService;

    @MockBean
    private TasklistRepository taskRepository;

    @Test
    void deveCriarTarefaQuandoCamposValidos() {
        UUID userId = UUID.randomUUID();
        User user = User.builder().id(userId).name("Alex").email("alex@email.com").build();
        TasklistEntity task = TasklistEntity.builder()
                .title("Comprar pão")
                .listName("Compras")
                .user(user)
                .build();

        when(taskRepository.save(any(TasklistEntity.class))).thenAnswer(inv -> inv.getArgument(0));

        TasklistEntity result = taskService.create(task);

        verify(taskRepository).save(task);
        assertThat(result.getTitle()).isEqualTo("Comprar pão");
        assertThat(result.getListName()).isEqualTo("Compras");
        assertThat(result.getUser()).isEqualTo(user);
    }

    @Test
    void deveLancarErroQuandoTituloNaoInformado() {
        TasklistEntity task = TasklistEntity.builder().listName("Compras").build();

        assertThatThrownBy(() -> taskService.create(task))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Título é obrigatório");
    }

    @Test
    void deveLancarErroQuandoNomeDaListaNaoInformado() {
        TasklistEntity task = TasklistEntity.builder().title("Comprar pão").build();

        assertThatThrownBy(() -> taskService.create(task))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Nome da lista é obrigatório");
    }

    @Test
    void deveLancarErroQuandoUsuarioNaoInformado() {
        TasklistEntity task = TasklistEntity.builder().title("Comprar pão").listName("Compras").build();

        assertThatThrownBy(() -> taskService.create(task))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Usuário é obrigatório");
    }

    @Test
    void deveEncontrarTarefaPorId() {
        UUID id = UUID.randomUUID();
        TasklistEntity task = TasklistEntity.builder().id(id).title("Comprar pão").listName("Compras").build();

        when(taskRepository.findById(id)).thenReturn(Optional.of(task));

        TasklistEntity result = taskService.findById(id);

        assertThat(result).isEqualTo(task);
    }

    @Test
    void deveLancarErroSeTarefaNaoExistir() {
        UUID id = UUID.randomUUID();
        when(taskRepository.findById(id)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> taskService.findById(id))
                .isInstanceOf(TaskNotFoundException.class)
                .hasMessageContaining("Tarefa não encontrada");
    }

}
