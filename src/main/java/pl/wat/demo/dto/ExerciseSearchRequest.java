package pl.wat.demo.dto;

import java.util.List;

public record ExerciseSearchRequest(
        String search,
        List<Integer> muscleIds
) {
}
