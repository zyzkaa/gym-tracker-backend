package pl.wat.demo.dto;

import java.util.List;

public record ExerciseDataResponse(
        String date,
        List<ExerciseDataSetResponse> sets,
        double volume,
        double orm,
        double maxLoad

) {
    public record ExerciseDataSetResponse(
            int order,
            int reps,
            double weight
    ) {}
}
