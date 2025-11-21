package pl.wat.demo.dto;

import pl.wat.demo.model.WorkoutSet;

import java.util.List;

public record ExerciseDataResponse(
        String date,
        List<ExerciseDataSetResponse> sets,
        double volume,
        double orm,
        double maxLoad

) {
    public record ExerciseDataSetResponse(
            int number,
            int reps,
            double weight
    ) {}
}
