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
import java.util.stream.Stream;

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
        return planMapper.toShortPlanList(plans);
    }

    public PlanResponse getPlanDetails(int planId, String userId) {
        UUID uuid = (userId == null) ? null : UUID.fromString(userId);
        Plan plan = planRepository.findById(planId).orElseThrow();
        if(plan.isPublic() || plan.getAuthor().getId().equals(uuid)) return planMapper.toResponse(plan, uuid);
        else throw new IllegalArgumentException();
    }

    public List<ShortPlanResponse> searchPlans(String search, String userId) {
        var plans = planRepository.findAllByIsPublicTrueAndNameContainingIgnoreCase(search);
        if(userId == null) {
            System.out.println("early return");
            return planMapper.toShortPlanList(plans);
        }
        UUID uuid = UUID.fromString(userId);
        User user = userRepository.findById(uuid).orElseThrow();
        List<Integer> userPlanIds = java.util.stream.Stream.concat(
                        planRepository.findPlansByAuthorId(uuid).stream(),
                        user.getSavedPlans().stream()
                ).map(Plan::getId).toList();
        System.out.println(userPlanIds);

        return planMapper.toShortPlanList(plans.stream().filter(plan -> !userPlanIds.contains(plan.getId())).collect(Collectors.toList()));
    }

    public void deletePlan(int planId, String userId) {
        Plan plan = planRepository.findById(planId).orElseThrow();
        System.out.println(plan.getAuthor().getUsername());
        if(plan.isPublic() || !plan.getAuthor().getId().toString().equals(userId)) return;
        System.out.println("deleting plan");
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

    public void savePlan(int planId, String userId){
        Plan plan = planRepository.findById(planId).orElseThrow();
        User user = userRepository.findById(UUID.fromString(userId)).orElseThrow();
        user.getSavedPlans().add(plan);
        userRepository.save(user);
    }
}
