package Project.ru.Neoflex.training.centre.api.factories;


import Project.ru.Neoflex.training.centre.api.dto.TaskDto;
import Project.ru.Neoflex.training.centre.store.entities.TaskEntity;
import org.springframework.scheduling.config.Task;
import org.springframework.stereotype.Component;

@Component
public class TaskDtoFactory {
    public TaskDto mapTaskDto(TaskEntity entity) {

        return TaskDto.builder()
                .id(entity.getId())
                .name(entity.getName())
                .createdAt(entity.getCreatedAt())
                .description(entity.getDescription())
                .build();







    }
}
