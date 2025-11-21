package pl.wat.demo.dto;

import pl.wat.demo.model.Exercise;

public record DayExerciseResponse(
        Exercise exercise,
        int sets,
        int reps,
        int position
) {
}
