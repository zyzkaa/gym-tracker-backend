package pl.wat.demo.dto;

import java.util.List;

public record PlanResponse(
        int id,
        String name,
        String description,
        UserResponse author,
        Boolean isPublic,
        Boolean isOwner,
        List<DayResponse> days
) {
}
