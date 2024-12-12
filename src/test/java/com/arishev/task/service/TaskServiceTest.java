package com.arishev.task.service;

import com.arishev.task.dto.TaskDTO;
import com.arishev.task.entity.Task;
import com.arishev.task.enums.Status;
import com.arishev.task.kafka.KafkaClientProducer;
import com.arishev.task.mapper.TaskMapper;
import com.arishev.task.repository.TaskRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class TaskServiceTest {

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private KafkaClientProducer kafkaProducer;

    @BeforeEach
    void config() {
        Task task = new Task();
        task.setId(1L);
        task.setTitle("Task_test_1");
        task.setUserId(1L);
        task.setDescription("test_description");
        task.setStatus(Status.NEW);

        when(taskRepository.save(task)).thenReturn(task);
        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));

        List<Task> tasks = new ArrayList<>();
        tasks.add(task);
        when(taskRepository.findAll()).thenReturn(tasks);
    }

    @Test
    void createTask() {

        TaskMapper mapper = new TaskMapper();

        TaskDTO taskDTO = new TaskDTO();

        taskDTO.setId(1L);
        taskDTO.setTitle("Task_test_1");
        taskDTO.setUserId(1L);
        taskDTO.setDescription("test_description");
        taskDTO.setStatus(Status.NEW);

        TaskService taskService = new TaskService(kafkaProducer, mapper, taskRepository);

        long taskId = taskService.createTask(taskDTO);

        Assertions.assertEquals(1L, taskId);
    }

    @Test
    void getTask() {

        TaskMapper mapper = new TaskMapper();

        TaskService taskService = new TaskService(kafkaProducer, mapper, taskRepository);

        Assertions.assertEquals(TaskDTO.class, taskService.getTask(1L).getClass());
        Assertions.assertEquals("Task_test_1", taskService.getTask(1L).getTitle());
    }

    @Test
    void getTaskThrowsException() {
        TaskMapper mapper = new TaskMapper();

        TaskService taskService = new TaskService(kafkaProducer, mapper, taskRepository);

        long wrongTaskId = 999L;
        Assertions.assertThrows(RuntimeException.class, () -> taskService.getTask(wrongTaskId));
    }

    @Test
    void updateTask() {

        TaskMapper mapper = new TaskMapper();

        TaskDTO taskDTOtoUpdate = new TaskDTO();
        taskDTOtoUpdate.setId(1L);
        taskDTOtoUpdate.setTitle("Updated_Task_test_1");
        taskDTOtoUpdate.setUserId(2L);
        taskDTOtoUpdate.setDescription("updated_test_description");
        taskDTOtoUpdate.setStatus(Status.STARTED);

        TaskService taskService = new TaskService(kafkaProducer, mapper, taskRepository);

        TaskDTO updatedTask = taskService.updateTask(taskDTOtoUpdate, 1L);

        Assertions.assertEquals("Updated_Task_test_1", updatedTask.getTitle());
        Assertions.assertEquals(2L, updatedTask.getUserId());
        Assertions.assertEquals("updated_test_description", updatedTask.getDescription());
        Assertions.assertEquals(Status.STARTED, updatedTask.getStatus());

    }

    @Test
    void updateTaskThrowsException() {
        TaskMapper mapper = new TaskMapper();

        final TaskDTO taskDTOtoUpdate = new TaskDTO();
        taskDTOtoUpdate.setId(1L);
        taskDTOtoUpdate.setTitle("Updated_Task_test_1");
        taskDTOtoUpdate.setUserId(2L);
        taskDTOtoUpdate.setDescription("updated_test_description");
        taskDTOtoUpdate.setStatus(Status.STARTED);

        TaskService taskService = new TaskService(kafkaProducer, mapper, taskRepository);

        long wrongTaskId = 999L;
        Assertions.assertThrows(RuntimeException.class, () -> taskService.updateTask(taskDTOtoUpdate, wrongTaskId));
    }


    @Test
    void getTasks() {
        TaskMapper mapper = new TaskMapper();
        TaskService taskService = new TaskService(kafkaProducer, mapper, taskRepository);

        Assertions.assertEquals(1, taskService.getTasks().size());
    }
}