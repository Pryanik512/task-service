package com.arishev.task.service;

import com.arishev.task.aspect.annotations.LogException;
import com.arishev.task.aspect.annotations.LogExecution;
import com.arishev.task.aspect.annotations.LogTaskResult;
import com.arishev.task.entity.Task;
import com.arishev.task.repository.TaskRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class TaskService {

    private TaskRepository taskRepository;

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

                    return taskRepository.save(t);
                }).orElse(taskRepository.save(updatedTask));
    }

    public void deleteTask(long id) {
         taskRepository.deleteById(id);
    }

    public List<Task> getTasks() {
        return taskRepository.findAll();
    }
}
