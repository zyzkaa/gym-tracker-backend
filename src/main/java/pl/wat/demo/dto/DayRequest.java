package pl.wat.demo.dto;

import java.util.List;

public record DayRequest (
    String name,
    Integer weekday,
    List<DayExerciseRequest> exercises
) {}
