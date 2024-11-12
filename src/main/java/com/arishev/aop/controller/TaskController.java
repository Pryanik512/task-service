package com.arishev.aop.controller;


import com.arishev.aop.entity.Task;
import com.arishev.aop.service.TaskService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

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

    @PutMapping("/{id}")
    public Task updateTask(@RequestBody Task task, @PathVariable long id) {
        return taskService.updateTask(task, id);
    }

    @DeleteMapping("/{id}")
    public void deleteTask(@PathVariable long id) {
         taskService.deleteTask(id);
    }
}
