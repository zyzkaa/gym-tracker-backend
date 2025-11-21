package pl.wat.demo.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "exercises_days")
@Getter
@Setter
public class ExerciseDay {
    @EmbeddedId
    @JsonIgnore
    private ExerciseDayId id = new ExerciseDayId();

    @ManyToOne
    @MapsId("dayId")
    @JoinColumn(name = "day_id")
    private Day day;

    @ManyToOne
    @MapsId("exerciseId")
    @JoinColumn(name = "exercise_id")
    private Exercise exercise;

    @Column(nullable = false)
    private int sets;
    private Integer repetitions;

    private int position;

    @Embeddable
    public static class ExerciseDayId implements Serializable {
        private int exerciseId;
        private int dayId;

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof ExerciseDayId that)) return false;
            return dayId == that.dayId && Objects.equals(exerciseId, that.exerciseId);
        }

        @Override
        public int hashCode() {
            return Objects.hash(exerciseId, dayId);
        }
    }
}

