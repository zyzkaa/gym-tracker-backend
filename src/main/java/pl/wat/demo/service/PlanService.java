package pl.wat.demo.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import pl.wat.demo.dto.*;
import pl.wat.demo.mapper.PlanMapper;
import pl.wat.demo.model.Exercise;
import pl.wat.demo.model.Plan;
import pl.wat.demo.model.User;
import pl.wat.demo.model.Weekday;
import pl.wat.demo.repository.*;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class PlanService {
    private final PlanRepository planRepository;
    private final UserRepository userRepository;
    private final PlanMapper planMapper;
    private final ExerciseRepository exerciseRepository;
    private final WeekdayRepository weekdayRepository;

    public int addPlan(PlanRequest planRequest, String userId) {
        Set<Integer> exerciseIds = planRequest.days().stream()
                .map(DayRequest::exercises)
                .flatMap(Collection::stream)
                .map(DayExerciseRequest::exerciseId)
                .collect(Collectors.toSet());
        List<Exercise> exercises = exerciseRepository.findAllById(exerciseIds);
        User user = userRepository.findById(UUID.fromString(userId)).orElseThrow();
        List<Weekday> weekdays = weekdayRepository.findAll();

        Map<Integer, Exercise> exerciseById = exercises.stream()
                .collect(Collectors.toMap(Exercise::getId, Function.identity()));
        Map<Integer, Weekday> weekdayById = weekdays.stream()
                .collect(Collectors.toMap(Weekday::getId, Function.identity()));

        Plan plan = planRepository.save(planMapper.toEntity(planRequest, user, exerciseById, weekdayById));
        return plan.getId();
    }

    public List<ShortPlanResponse> getUsersPlans(String userId) {
        UUID uuid = UUID.fromString(userId);
        User user = userRepository.findById(uuid).orElseThrow();
        List<Plan> plans = java.util.stream.Stream.concat(
                        planRepository.findPlansByAuthorId(uuid).stream(),
                        user.getSavedPlans().stream()
                )
                .toList();
        List<ShortPlanResponse> planss = planMapper.toShortPlanList(plans);
        planss.forEach(p -> p.days().forEach( d -> System.out.println(d.name())));
        return planss;
    }

    public PlanResponse getPlanDetails(int planId, String userId) {
        Plan plan = planRepository.findById(planId).orElseThrow();
        if(plan.isPublic() || plan.getAuthor().getId().toString().equals(userId)) return planMapper.toResponse(plan, UUID.fromString(userId));
        else throw new IllegalArgumentException();
    }

    public List<ShortPlanResponse> searchPlans(String search) {
        return planMapper.toShortPlanList(planRepository.findAllByNameContainingIgnoreCase(search));
    }

    public void deletePlan(int planId, String userId) {
        Plan plan = planRepository.findById(planId).orElseThrow();
        if(plan.isPublic() || !plan.getAuthor().getId().toString().equals(userId)) return;
        planRepository.deleteById(planId);
    }

    public void updatePlan(PlanRequest planRequest, String userId) {
        UUID uuid = UUID.fromString(userId);
        User user = userRepository.findById(uuid).orElseThrow();
        List<Plan> plans = java.util.stream.Stream.concat(
                        planRepository.findPlansByAuthorId(uuid).stream(),
                        user.getSavedPlans().stream()
                )
                .toList();
    }

    public void changePlanPublicity(int planId, String userId) {
        Plan plan = planRepository.findById(planId).orElseThrow();
        if(!plan.getAuthor().getId().toString().equals(userId)) return;
        plan.setPublic(!plan.isPublic());
    }

    public void generatePlan(PlanPreferenceRequest preferences, String userId) {

    }
}
