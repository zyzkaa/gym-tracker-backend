package pl.wat.demo.dto;

public record DayExerciseRequest (
        int exerciseId,
        int sets,
        int reps,
        int order
) {}
