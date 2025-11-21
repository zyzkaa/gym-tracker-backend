package pl.wat.demo.dto;

import java.time.LocalDateTime;
import java.util.List;

public record WorkoutResponse(
        int id,
        LocalDateTime date,
        String dayName,
        String planName,
        List<Exercise> exercises
) {
    public record Exercise(
            pl.wat.demo.model.Exercise exercise,
            int order,
            List<Set> sets
    ){}

    public record Set(
         int setNumber,
         int repetitions,
         Double weight
    ){}
}
