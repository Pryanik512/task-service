package com.arishev.task.dto;

import com.arishev.task.enums.Status;

import lombok.Data;

@Data
public class TaskDTO {

    private Long id;

    private String title;
    private String description;
    private Long userId;
    private Status status;
}
