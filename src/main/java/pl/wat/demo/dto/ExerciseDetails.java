package pl.wat.demo.dto;

import pl.wat.demo.model.Muscle;

import java.util.List;

public record ExerciseDetails(
        int id,
        String name,
        List<Muscle> muscles,
        String description,
        String imageUrl
) {
}
