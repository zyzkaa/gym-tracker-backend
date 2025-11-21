package pl.wat.demo.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "sets")
@Getter
@Setter
@NoArgsConstructor
public class WorkoutSet {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "set_number", nullable = false)
    private int setNumber;

    private int repetitions;

    private Double weight;

    @ManyToOne
    @JoinColumn(nullable = false, name = "workouts_exercises_id")
    private WorkoutExercise workoutExercise;
}
