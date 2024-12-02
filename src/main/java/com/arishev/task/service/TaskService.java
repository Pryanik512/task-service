package com.arishev.task.service;

import com.arishev.logging_starter.aspect.annotations.LogException;
import com.arishev.logging_starter.aspect.annotations.LogExecution;
import com.arishev.logging_starter.aspect.annotations.LogTaskResult;
import com.arishev.task.dto.TaskDTO;
import com.arishev.task.entity.Task;
import com.arishev.task.kafka.KafkaClientProducer;
import com.arishev.task.mapper.TaskMapper;
import com.arishev.task.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class TaskService {

    @Value("${task-service.kafka.task-status-topic}")
    private String taskStatusTopic;

    private final KafkaClientProducer kafkaProducer;

    private final TaskMapper mapper;

    private final TaskRepository taskRepository;

    @LogExecution
    public long createTask(TaskDTO task) {

        return taskRepository.save(mapper.toEntity(task)).getId();
    }

    @LogException
    @LogTaskResult
    public TaskDTO getTask(long id) {
        return mapper.toDto(taskRepository.findById(id).orElseThrow(() -> new RuntimeException("Task wasn't found!")));
    }

    public TaskDTO updateTask(TaskDTO updatedTask, long id) {

        Task taskEntity = taskRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Task wasn't found!"));

        notifyStatusChanged(updatedTask, taskEntity);

        taskEntity.setDescription(updatedTask.getDescription());
        taskEntity.setTitle(updatedTask.getTitle());
        taskEntity.setUserId(updatedTask.getUserId());
        taskEntity.setStatus(updatedTask.getStatus());

        return mapper.toDto(taskRepository.save(taskEntity));
    }

    public void deleteTask(long id) {
         taskRepository.deleteById(id);
    }

    public List<TaskDTO> getTasks() {
        return mapper.toDtoList(taskRepository.findAll());
    }

    private void notifyStatusChanged(TaskDTO updatedTask, Task task) {

        boolean isStatusChanged = !task.getStatus().equals(updatedTask.getStatus());

        if (isStatusChanged) {
            TaskDTO taskToKafka = new TaskDTO();
            taskToKafka.setId(task.getId());
            taskToKafka.setStatus(updatedTask.getStatus());

            kafkaProducer.sendTo(taskStatusTopic, taskToKafka);
        }

    }
}
