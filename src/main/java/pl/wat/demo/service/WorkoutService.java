package pl.wat.demo.service;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import pl.wat.demo.dto.ShortWorkoutResponse;
import pl.wat.demo.dto.WorkoutRequest;
import pl.wat.demo.dto.WorkoutResponse;
import pl.wat.demo.dto.WorkoutsPaginationResponse;
import pl.wat.demo.mapper.WorkoutMapper;
import pl.wat.demo.model.*;
import pl.wat.demo.repository.DayRepository;
import pl.wat.demo.repository.ExerciseRepository;
import pl.wat.demo.repository.UserRepository;
import pl.wat.demo.repository.WorkoutRepository;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.temporal.TemporalUnit;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;


@Service
@AllArgsConstructor
public class WorkoutService {
    private final WorkoutRepository workoutRepository;
    private final WorkoutMapper workoutMapper;
    private final UserRepository userRepository;
    private final DayRepository dayRepository;
    private final ExerciseRepository exerciseRepository;

    public WorkoutsPaginationResponse getWorkoutsHistory(int amount, int offset, String userId){
        Pageable pageable = PageRequest.of(offset, amount, Sort.by("date").descending());
        Page<Workout> result = workoutRepository.findAllByUserId(UUID.fromString(userId), pageable);
        return new WorkoutsPaginationResponse(workoutMapper.toShortResponses(result.getContent()), result.getTotalPages());
    }

    public int addWorkout(String userId, WorkoutRequest workoutRequest){
        User user = userRepository.findById(UUID.fromString(userId)).orElseThrow();
        Day day = dayRepository.findById(workoutRequest.dayId()).orElseThrow();
        Set<Integer> exerciseIds = workoutRequest.exercises().stream().map(WorkoutRequest.Exercise::id).collect(Collectors.toSet());
        List<Exercise> exercises = exerciseRepository.findAllById(exerciseIds);
        Map<Integer, Exercise> exercisesById = exercises.stream()
                .collect(Collectors.toMap(Exercise::getId, Function.identity()));

        Workout workout = workoutRepository.save(workoutMapper.toWorkout(workoutRequest, exercisesById, user, day));
        return workout.getId();

//        Workout workout = workoutMapper.toWorkout(workoutRequest, exercisesById, user, day);
//        int weeks = 30;
//        workout.setDate(LocalDateTime.now().minus(Duration.ofDays((weeks*7)+6)));
//
//        for (int i = 0; i < weeks - 1; i++) {
//            workoutRepository.save(workout);
//            Workout newWorkout = new Workout();
//            newWorkout.setUser(workout.getUser());
//            newWorkout.setDay(workout.getDay());
//            newWorkout.setExercises(workout.getExercises());
//            newWorkout.setDate(workout.getDate().plus(Duration.ofDays(7)));
//            newWorkout.setExercises(
//                    newWorkout.getExercises().stream().map(exercise -> {
//                        WorkoutExercise workoutExercise = new WorkoutExercise();
//                        workoutExercise.setWorkout(newWorkout);
//                        workoutExercise.setExercise(exercise.getExercise());
//                        workoutExercise.setExerciseOder(exercise.getExerciseOder());
//                        double random = Math.random();
//                        workoutExercise.setSets(
//                                exercise.getSets().stream().map(set -> {
//                                    WorkoutSet sett = new WorkoutSet();
//                                    sett.setWorkoutExercise(workoutExercise);
//                                    if(random < 0.7) {
//                                        sett.setWeight(set.getWeight() + 2.5);
//                                    } else {
//                                        sett.setWeight(set.getWeight());
//                                    }
//                                    double random2 = Math.random();
//                                    if(random2 < 0.2) {
//                                        sett.setRepetitions(set.getRepetitions() - 1);
//                                    } else if(random2 < 0.4) {
//                                        sett.setRepetitions(set.getRepetitions() + 1);
//                                    } else {
//                                        sett.setRepetitions(set.getRepetitions());
//                                    }
//                                    sett.setSetNumber(set.getSetNumber());
//                                    return sett;
//                                }).collect(Collectors.toCollection(ArrayList::new))
//                        );
//                        return workoutExercise;
//                    }).collect(Collectors.toCollection(ArrayList::new))
//            );
//
//            workout = newWorkout;
//
//            System.out.println(workout.getDate().toLocalDate().toString());
//            workout.getExercises().forEach(exercise -> {
//                System.out.println(exercise.getExercise().getName());
//                exercise.getSets().forEach(set -> {
//                    System.out.print(set.getWeight() + " ");
//                });
//            });
//            System.out.print("\n");
//        }
//
//        return workoutRepository.save(workout).getId();
    }

    public WorkoutResponse getById(int workoutId){
        Workout workout = workoutRepository.findById(workoutId).orElseThrow();
        return workoutMapper.toWorkoutResponse(workout);
    }
}
