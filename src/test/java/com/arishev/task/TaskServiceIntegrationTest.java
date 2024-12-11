package com.arishev.task;

import com.arishev.task.dto.TaskDTO;
import com.arishev.task.entity.Task;
import com.arishev.task.enums.Status;
import com.arishev.task.repository.TaskRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class TaskServiceIntegrationTest extends AbstractTestContainer {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TaskRepository taskRepository;

    @BeforeEach
    public void config() {

        taskRepository.deleteAll();

        Task task1 = new Task();
        task1.setTitle("Task_test_1");
        task1.setUserId(1L);
        task1.setDescription("test_description_1");
        task1.setStatus(Status.NEW);

        Task task2 = new Task();
        task2.setTitle("Task_test_2");
        task2.setUserId(2L);
        task2.setDescription("test_description_2");
        task2.setStatus(Status.NEW);

        List<Task> tasks = new ArrayList<>();
        tasks.add(task1);
        tasks.add(task2);

        taskRepository.saveAll(tasks);
    }

    @Test
    public void getTaskById() throws Exception {

        mockMvc.perform(get("/tasks/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.title", is("Task_test_1")));

    }

    @Test
    public void getAllTasks() throws Exception {

        mockMvc.perform(get("/tasks"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].title", is("Task_test_1")))
                .andExpect(jsonPath("$[1].id", is(2)))
                .andExpect(jsonPath("$[1].title", is("Task_test_2")))
        ;

    }

    @Test
    public void updateTaskById() throws Exception {

        TaskDTO taskDTO = new TaskDTO();
        taskDTO.setTitle("Updated_Task_test_1");
        taskDTO.setUserId(2L);
        taskDTO.setDescription("updated_test_description");
        taskDTO.setStatus(Status.STARTED);

        ObjectMapper mapper = new ObjectMapper();


        String requestBodyTask = mapper.writeValueAsString(taskDTO);

        mockMvc.perform(put("/tasks/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBodyTask))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.title", is("Updated_Task_test_1")));
    }

    @Test
    public void deleteDyId() throws Exception {
        mockMvc.perform(delete("/tasks/1"))
                .andExpect(status().isOk());

        Assertions.assertEquals(1, taskRepository.findAll().size());

    }

}
