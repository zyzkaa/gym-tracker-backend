package pl.wat.demo.dto;

import java.time.LocalDateTime;

import java.util.List;

public record ShortWorkoutResponse(
        int id,
        LocalDateTime date,
        String dayName,
        String planName,
        List<String> exercises
){
}
