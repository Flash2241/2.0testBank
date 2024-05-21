package Project.ru.Neoflex.training.centre.api.controllers;

import Project.ru.Neoflex.training.centre.api.dto.ProjectDto;
import Project.ru.Neoflex.training.centre.api.exceptions.BadRequestException;
import Project.ru.Neoflex.training.centre.api.exceptions.NotFoundException;
import Project.ru.Neoflex.training.centre.api.factories.ProjectDtoFactory;
import Project.ru.Neoflex.training.centre.store.entities.ProjectEntity;
import Project.ru.Neoflex.training.centre.store.repositories.ProjectRepository;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Transactional
@RestController
public class ProjectController {

    ProjectRepository projectRepository;

    ProjectDtoFactory projectDtoFactory;
    public static final String FETCH_PROJECT ="/api/project";
    public static final String CREATE_PROJECT = "/api/projects";
    public static final String EDIT_PROJECT = "/api/projects/{project_id}";

    @PostMapping(CREATE_PROJECT)
    public List<ProjectDto> fetchProjects(@RequestParam String name)
@PostMapping(CREATE_PROJECT)
public ProjectDto createProject(@RequestParam String name) {

    if(name.trim().isEmpty()){
        throw new BadRequestException("Project name cannot be empty");
    }

    projectRepository
            .fingByName(name)
            .ifPresent(project -> {
                throw new BadRequestException(String.format("Project \"%s\" already exists", name));

            });

    ProjectEntity project = projectRepository.saveAndFlush(
            ProjectEntity.builder()
                    .name(name)
                    .build()
    );

    return projectDtoFactory.makeProjectDto(project);
}

@PatchMapping (EDIT_PROJECT)
public ProjectDto editPatch(
        @PathVariable("projectId") Long projectId,
        @RequestParam String name) {

    if(name.trim().isEmpty()){
        throw new BadRequestException("Project name cannot be empty");
    }


    ProjectEntity project =projectRepository
            .findById(projectId)
                    .orElseThrow(() ->
                            new NotFoundException(
                                    String.format(
                                            "Project with \"%s\" doesn`t exist.",
                                            projectId)));
    projectRepository
            .fingByName(name)
            .filter(anotherProject -> !Object.equals(anotherProject.getId(), projectId))
            .ifPresent(anotherProject  -> {
                    throw new BadRequestException(String.format("Project \"%s\" already exists", name));
    });


    project.setName(name);

    project=projectRepository.saveAndFlush(project);

    return projectDtoFactory.makeProjectDto(project);
}



}
