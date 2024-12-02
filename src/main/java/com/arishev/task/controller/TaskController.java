package com.arishev.task.controller;


import com.arishev.logging_starter.aspect.annotations.EndPointDataToLog;
import com.arishev.logging_starter.aspect.annotations.LogProfiling;
import com.arishev.task.dto.TaskDTO;
import com.arishev.task.mapper.TaskMapper;
import com.arishev.task.service.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tasks")
@RequiredArgsConstructor
public class TaskController {

    private final TaskService taskService;

    private final TaskMapper mapper;

    @PostMapping
    @EndPointDataToLog
    public long createTask(@RequestBody TaskDTO taskDto) {
        return taskService.createTask(taskDto);
    }

    @GetMapping("/{id}")
    @EndPointDataToLog
    public TaskDTO getTask(@PathVariable long id) {

        return taskService.getTask(id);

    }

    @GetMapping
    @LogProfiling
    @EndPointDataToLog
    public List<TaskDTO> getTasks() {

        return taskService.getTasks();

    }

    @PutMapping("/{id}")
    @EndPointDataToLog
    public TaskDTO updateTask(@RequestBody TaskDTO taskDto, @PathVariable long id) {

        return taskService.updateTask(taskDto, id);
    }

    @DeleteMapping("/{id}")
    @EndPointDataToLog
    public void deleteTask(@PathVariable long id) {
         taskService.deleteTask(id);
    }
}
