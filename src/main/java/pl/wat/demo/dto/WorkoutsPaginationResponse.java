package pl.wat.demo.dto;

import java.util.List;

public record WorkoutsPaginationResponse(
        List<ShortWorkoutResponse> workouts,
        int totalPages
) {
}
