package pl.wat.demo.dto;

import java.time.LocalDateTime;
import java.util.List;

public record WorkoutRequest(
        LocalDateTime date,
        Integer dayId,
        List<Exercise> exercises
) {
    public record Exercise(
            int id,
            int order,
            List<Set> sets
    ){}

    public record Set(
            int order,
            int reps,
            Double weight
    ){}
}
