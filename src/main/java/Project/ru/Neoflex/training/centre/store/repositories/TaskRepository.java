package Project.ru.Neoflex.training.centre.store.repositories;

import Project.ru.Neoflex.training.centre.store.entities.TaskEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskRepository  extends JpaRepository<TaskEntity,Long> {
}
