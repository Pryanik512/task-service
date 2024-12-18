package com.arishev.task.entity;


import com.arishev.task.enums.Status;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "tasks")
@Data
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String description;
    private Long userId;

    @Enumerated(EnumType.STRING)
    private Status status;

}
