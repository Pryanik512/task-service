package com.arishev.aop.service;

import com.arishev.aop.entity.Task;
import com.arishev.aop.repository.TaskRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class TaskService {

    private TaskRepository taskRepository;

    public long createTask(Task task) {

        return taskRepository.save(task).getId();
    }

    public Task getTask(long id) {
        return taskRepository.findById(id).orElse(null);
    }

    public Task updateTask(Task updatedTask, long id) {
        return taskRepository.findById(id)
                .map(t -> {
                    t.setDescription(updatedTask.getDescription());
                    t.setTitle(updatedTask.getTitle());
                    t.setUserId(updatedTask.getUserId());

                    return taskRepository.save(t);
                }).orElse(null);
    }

    public void deleteTask(long id) {
         taskRepository.deleteById(id);
    }
}
