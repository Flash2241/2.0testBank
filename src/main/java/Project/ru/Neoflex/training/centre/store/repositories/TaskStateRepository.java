package Project.ru.Neoflex.training.centre.store.repositories;

import Project.ru.Neoflex.training.centre.store.entities.TaskStateEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskStateRepository extends JpaRepository<TaskStateEntity , Long> {
}
