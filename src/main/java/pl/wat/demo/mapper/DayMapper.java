package pl.wat.demo.mapper;

import org.springframework.stereotype.Component;
import pl.wat.demo.dto.DayExerciseResponse;
import pl.wat.demo.dto.DayResponse;
import pl.wat.demo.model.Day;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class DayMapper {
    public DayResponse toResponse(Day day) {
        List<DayExerciseResponse> exercises = day.getExercises().stream().map(
                exercise -> {
                    return new DayExerciseResponse(
                            exercise.getExercise(),
                            exercise.getSets(),
                            exercise.getRepetitions(),
                            exercise.getPosition());
                }
        ).collect(Collectors.toCollection(ArrayList::new));

        return new DayResponse(
                day.getName(),
                day.getId(),
                (day.getWeekday() == null) ? null : day.getWeekday().getId() - 1,
                exercises
        );
    }

    public List<DayResponse> toResponses(List<Day> days) {
        return days.stream().map(this::toResponse).collect(Collectors.toList());
    }
}
