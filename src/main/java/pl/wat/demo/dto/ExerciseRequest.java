package pl.wat.demo.dto;

import java.util.List;

public record ExerciseRequest(
        String name,
        String description,
        List<Integer> muscleIds
) {
}
