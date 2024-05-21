package Project.ru.Neoflex.training.centre.api.factories;

import Project.ru.Neoflex.training.centre.api.dto.ProjectDto;
import Project.ru.Neoflex.training.centre.store.entities.ProjectEntity;
import org.springframework.stereotype.Component;

@Component
public class ProjectDtoFactory {


    public ProjectDto makeProjectDto(ProjectEntity entity) {

        return ProjectDto.builder()
                .id(entity.getId())
                .name(entity.getName())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();

    }

}
