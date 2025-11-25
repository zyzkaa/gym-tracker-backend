package pl.wat.demo.mapper;

import org.springframework.stereotype.Component;
import pl.wat.demo.dto.ExerciseDetails;
import pl.wat.demo.model.Exercise;

@Component
public class ExerciseMapper {
    public ExerciseDetails toExerciseDetails(Exercise exercise) {
        return new ExerciseDetails(
                exercise.getId(),
                exercise.getName(),
                exercise.getMuscles(),
                exercise.getDescription(),
                exercise.getImage()
        );
    }
}
