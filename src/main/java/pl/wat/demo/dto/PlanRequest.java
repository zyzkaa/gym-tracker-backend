package pl.wat.demo.dto;

import java.util.List;

public record PlanRequest(
        String name,
        String description,
        Boolean isPublic,
        List<DayRequest> days
) {
}
