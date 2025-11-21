package pl.wat.demo.dto;

import jakarta.annotation.Nullable;

import java.util.List;
import java.util.Map;

public record ShortPlanResponse(
        int id,
        String name,
        List<DayItem> days
) {
    public record DayItem(@Nullable Integer weekdayId, String name, int dayId) {}
}
