package Project.ru.Neoflex.training.centre.store.entities;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;


@Getter
@Setter
@Builder   //автоматически генерирует класc builder
@NoArgsConstructor   //конструктор без аргументов
@AllArgsConstructor //конструктор со всеми аргументами
@FieldDefaults(level = AccessLevel.PRIVATE) //автоматически делает поля private
@Entity
@Table(name = "project")             //анотация к сущности
public class ProjectEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)   //автоматическое создание id в момент инициализации запроса бд по strategy
    Long id;

    @Column(unique = true)     //Условие уникальности поля в рамках системы
    String name;

    @Builder.Default
    Instant updatedAt=Instant.now();

    @Builder.Default
    Instant createdAt = Instant.now();

    @Builder.Default
    @OneToMany
    @JoinColumn(name="project_id",referencedColumnName = "id") // в TaskStateEntity будет создано поле Project ID по которому соберем список
     List<TaskStateEntity> taskStates = new ArrayList<>();  //инициализируем пустой массив


}

