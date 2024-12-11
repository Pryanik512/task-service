package com.arishev.task.mapper;

import com.arishev.task.dto.TaskDTO;
import com.arishev.task.entity.Task;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class TaskMapper {


   public TaskDTO toDto(Task task) {
      if ( task == null ) {
         return null;
      }

      TaskDTO taskDTO = new TaskDTO();

      taskDTO.setId( task.getId() );
      taskDTO.setTitle( task.getTitle() );
      taskDTO.setDescription( task.getDescription() );
      taskDTO.setUserId( task.getUserId() );
      taskDTO.setStatus( task.getStatus() );

      return taskDTO;
   }


   public List<TaskDTO> toDtoList(List<Task> tasks) {
      if ( tasks == null ) {
         return null;
      }

      List<TaskDTO> list = new ArrayList<TaskDTO>( tasks.size() );
      for ( Task task : tasks ) {
         list.add( toDto( task ) );
      }

      return list;
   }


   public Task toEntity(TaskDTO taskDTO) {
      if ( taskDTO == null ) {
         return null;
      }

      Task task = new Task();

      task.setId( taskDTO.getId() );
      task.setTitle( taskDTO.getTitle() );
      task.setDescription( taskDTO.getDescription() );
      task.setUserId( taskDTO.getUserId() );
      task.setStatus( taskDTO.getStatus() );

      return task;
   }


}
