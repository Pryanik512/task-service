package com.arishev.task.controller;


import com.arishev.task.entity.Task;
import com.arishev.task.service.TaskService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tasks")
@AllArgsConstructor
public class TaskController {

    private TaskService taskService;

    @PostMapping
    public long createTask(@RequestBody Task task) {
        return taskService.createTask(task);
    }

    @GetMapping("/{id}")
    public Task getTask(@PathVariable long id) {

        return taskService.getTask(id);

    }

    @GetMapping
    public List<Task> getTasks() {

        return taskService.getTasks();

    }

    @PutMapping("/{id}")
    public Task updateTask(@RequestBody Task task, @PathVariable long id) {
        return taskService.updateTask(task, id);
    }

    @DeleteMapping("/{id}")
    public void deleteTask(@PathVariable long id) {
         taskService.deleteTask(id);
    }
}
