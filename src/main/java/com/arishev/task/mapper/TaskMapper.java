package com.arishev.task.mapper;

import com.arishev.task.dto.TaskDTO;
import com.arishev.task.entity.Task;
import org.mapstruct.Mapper;

import org.mapstruct.MappingConstants;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface TaskMapper {


   TaskDTO toDto(Task task);

   List<TaskDTO> toDtoList(List<Task> tasks);

   Task toEntity(TaskDTO taskDTO);


}
