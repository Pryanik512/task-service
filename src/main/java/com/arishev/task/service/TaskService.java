package com.arishev.task.service;

import com.arishev.task.aspect.annotations.LogException;
import com.arishev.task.aspect.annotations.LogExecution;
import com.arishev.task.aspect.annotations.LogTaskResult;
import com.arishev.task.dto.TaskDTO;
import com.arishev.task.entity.Task;
import com.arishev.task.kafka.KafkaClientProducer;
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

    private final TaskRepository taskRepository;

    @LogExecution
    public long createTask(Task task) {

        return taskRepository.save(task).getId();
    }

    @LogException
    @LogTaskResult
    public Task getTask(long id) {
        return taskRepository.findById(id).orElseThrow(() -> new RuntimeException("Task wasn't found!"));
    }

    public Task updateTask(Task updatedTask, long id) {


        return taskRepository.findById(id)
                .map(t -> {
                    t.setDescription(updatedTask.getDescription());
                    t.setTitle(updatedTask.getTitle());
                    t.setUserId(updatedTask.getUserId());
                    t.setStatus(updatedTask.getStatus());

                    return taskRepository.save(t);
                }).orElseThrow();
    }

    public void deleteTask(long id) {
         taskRepository.deleteById(id);
    }

    public List<Task> getTasks() {
        return taskRepository.findAll();
    }

    public void notifyStatusChanged(TaskDTO taskDTO, long id) {

        Task task = taskRepository.findById(id).orElseThrow();

        boolean isStatusChanged = !task.getStatus().equals(taskDTO.getStatus());

        if (isStatusChanged) {
            TaskDTO taskToKafka = new TaskDTO();
            taskToKafka.setId(task.getId());
            taskToKafka.setStatus(taskDTO.getStatus());

            kafkaProducer.sendTo(taskStatusTopic, taskToKafka);
        }

    }
}
