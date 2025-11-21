package pl.wat.demo.dto;

import java.util.List;

public record DayResponse(
        String name,
        Integer weekday,
        List<DayExerciseResponse> exercises
) {
}
