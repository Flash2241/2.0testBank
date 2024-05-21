package Project.ru.Neoflex.training.centre.store.repositories;

import Project.ru.Neoflex.training.centre.store.entities.ProjectEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

//класс для работы с сущностями
public interface ProjectRepository extends JpaRepository<ProjectEntity, Long> {

Optional<ProjectEntity>fingByName(String projectName);



}
