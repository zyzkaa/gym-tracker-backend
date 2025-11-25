package pl.wat.demo.dto;

import java.util.List;

public record DayResponse(
        String name,
        Integer id,
        Integer weekday,
        List<DayExerciseResponse> exercises
) {
}
