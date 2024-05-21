package Project.ru.Neoflex.training.centre.api.factories;


import Project.ru.Neoflex.training.centre.api.dto.TaskStateDto;
import Project.ru.Neoflex.training.centre.store.entities.TaskStateEntity;
import org.springframework.stereotype.Component;

@Component
public class TaskStateDtoFactory {
    public TaskStateDto makeTaskStateDto(TaskStateEntity entity) {

        return TaskStateDto.builder()
                .id(entity.getId())
                .name(entity.getName())
                .createdAt(entity.getCreatedAt())
                .ordinal(entity.getOrdinal())
                .build();
    }
}
