package Project.ru.Neoflex.training.centre.store.entities;


import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor   //конструктор без аргументов
@AllArgsConstructor //конструктор со всеми аргументами
@FieldDefaults(level = AccessLevel.PRIVATE) //автоматически делает поля private
@Entity
@Table(name = "task")             //анотация к сущности
public class TaskStateEntity {


    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)   //автоматическое создание id в момент инициализации запроса бд по strategy
    Long id;

    @Column(unique = true)     //Условие уникальности поля в рамках системы
     String name;

    Long ordinal;

    @Builder.Default
    Instant createdAt = Instant.now();

    @Builder.Default
    @OneToMany
            @JoinColumn(name="tack_state_id", referencedColumnName = "id")
    List<TaskStateEntity> tasks = new ArrayList<>();









}
