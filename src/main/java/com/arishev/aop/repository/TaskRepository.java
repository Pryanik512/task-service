package com.arishev.aop.repository;

import com.arishev.aop.entity.Task;
import org.springframework.data.repository.CrudRepository;

public interface TaskRepository extends CrudRepository<Task, Long> {


}
