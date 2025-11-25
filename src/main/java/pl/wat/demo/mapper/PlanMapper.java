package pl.wat.demo.mapper;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import pl.wat.demo.dto.*;
import pl.wat.demo.model.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
@AllArgsConstructor
public class PlanMapper {
    private final DayMapper dayMapper;
    private final UserMapper userMapper;

    public Plan toEntity(PlanRequest planRequest, User user, Map<Integer, Exercise> exerciseMap, Map<Integer, Weekday> weekdayMap) {
        Plan plan = new Plan();
        plan.setName(planRequest.name());
        plan.setDescription(planRequest.description());
        plan.setPublic(planRequest.isPublic());
        plan.setAuthor(user);

        List<Day> days = planRequest.days().stream().map(
                requestDay -> {
                    Day day = new Day();
                    day.setName(requestDay.name());
                    day.setPlan(plan);
                    if (requestDay.weekday() != null) {
                        day.setWeekday(weekdayMap.get(requestDay.weekday() + 1));
                    }

                    List<ExerciseDay> exercises = requestDay.exercises().stream().map(
                            requestExercise -> {
                                ExerciseDay exerciseDay = new ExerciseDay();
                                exerciseDay.setSets(requestExercise.sets());
                                exerciseDay.setRepetitions(requestExercise.reps());
                                exerciseDay.setDay(day);
                                exerciseDay.setExercise(exerciseMap.get(requestExercise.exerciseId()));
                                exerciseDay.setPosition(requestExercise.order());
                                return exerciseDay;
                            }
                    ).collect(Collectors.toCollection(ArrayList::new));

                    day.setExercises(exercises);
                    return day;
                }
        ).collect(Collectors.toCollection(ArrayList::new));

        plan.setDays(days);
        return plan;
    }

    public PlanResponse toResponse(Plan plan, UUID userId) {
        List<DayResponse> days = dayMapper.toResponses(plan.getDays());

        return new PlanResponse(
                plan.getId(),
                plan.getName(),
                plan.getDescription(),
                userMapper.toResponse(plan.getAuthor()),
                plan.isPublic(),
                userId != null && userId.equals(plan.getAuthor().getId()),
                days
        );
    }

    public List<ShortPlanResponse> toShortPlanList(List<Plan> planList) {
        return planList.stream().map(
                plan -> new ShortPlanResponse(
                        plan.getId(),
                        plan.getName(),
                        plan.getDays().stream().map(
                                day -> {
                                    Integer weekdayId = (day.getWeekday() != null) ? day.getWeekday().getId() : null;
                                    return new ShortPlanResponse.DayItem(
                                            weekdayId,
                                            day.getName(),
                                            day.getId()
                                    );
                                }

                        ).collect(Collectors.toCollection(ArrayList::new))
                )
        ).collect(Collectors.toCollection(ArrayList::new));
    }
}
