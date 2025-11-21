package pl.wat.demo.mapper;

import org.springframework.stereotype.Component;
import pl.wat.demo.dto.ExerciseDataResponse;
import pl.wat.demo.dto.ShortWorkoutResponse;
import pl.wat.demo.dto.WorkoutRequest;
import pl.wat.demo.dto.WorkoutResponse;
import pl.wat.demo.model.*;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class WorkoutMapper {
    public List<ShortWorkoutResponse> toShortResponses(List<Workout> workouts) {
        return workouts.stream().map(
                w -> new ShortWorkoutResponse(
                        w.getId(),
                        w.getDate(),
                        w.getDay() == null ? "" : w.getDay().getName(),
                        (w.getDay() == null || w.getDay().getPlan() == null)
                                ? "" : w.getDay().getPlan().getName(),
                        w.getExercises().stream().map(
                                e -> e.getExercise().getName()
                        ).collect(Collectors.toCollection(ArrayList::new))
                )
        ).collect(Collectors.toCollection(ArrayList::new));
    }

    public Workout toWorkout(WorkoutRequest workoutRequest, Map<Integer, Exercise> exerciseMap, User user, Day day) {
        Workout workout = new Workout();
        workout.setDate(workoutRequest.date());
        workout.setDay(day);
        workout.setUser(user);
        workout.setExercises(
                workoutRequest.exercises().stream().map(
                        exercise -> {
                            WorkoutExercise workoutExercise = new WorkoutExercise();
                            workoutExercise.setExercise(exerciseMap.get(exercise.id()));
                            workoutExercise.setWorkout(workout);
                            workoutExercise.setExerciseOder(exercise.order());
                            workoutExercise.setSets(exercise.sets().stream().map(
                                    set -> {
                                        WorkoutSet workoutSet = new WorkoutSet();
                                        workoutSet.setWorkoutExercise(workoutExercise);
                                        workoutSet.setSetNumber(set.order() + 1);
                                        workoutSet.setRepetitions(set.reps());
                                        workoutSet.setWeight(set.weight());
                                        return workoutSet;
                                    }
                            ).collect(Collectors.toCollection(ArrayList::new)));
                            return workoutExercise;
                        }
                ).collect(Collectors.toCollection(ArrayList::new))
        );

        return workout;
    }

    public WorkoutResponse toWorkoutResponse(Workout workout) {
        return new WorkoutResponse(
                workout.getId(),
                workout.getDate(),
                workout.getDay() == null ? "" : workout.getDay().getName(),
                (workout.getDay() == null || workout.getDay().getPlan() == null)
                        ? "" : workout.getDay().getPlan().getName(),
                workout.getExercises().stream().map(
                        exercise -> new WorkoutResponse.Exercise(
                                exercise.getExercise(),
                                exercise.getExerciseOder(),
                                exercise.getSets().stream().map(
                                        set -> new WorkoutResponse.Set(
                                                set.getSetNumber(),
                                                set.getRepetitions(),
                                                set.getWeight()
                                        )
                                ).collect(Collectors.toCollection(ArrayList::new))
                        )
                ).collect(Collectors.toCollection(ArrayList::new))
        );
    }

    public List<ExerciseDataResponse> toExerciseDataList(List<WorkoutExercise> workoutExercises) {
        return workoutExercises.stream().map(
                we -> new ExerciseDataResponse(
                        we.getWorkout().getDate().toString(),
                        we.getSets().stream().map(
                                s -> new ExerciseDataResponse.ExerciseDataSetResponse(
                                        s.getSetNumber(),
                                        s.getRepetitions(),
                                        s.getWeight()
                                )
                        ).collect(Collectors.toCollection(ArrayList::new)),
                        we.getSets().stream().mapToDouble(
                                s -> s.getWeight() * s.getRepetitions()
                        ).sum(),
                        we.getSets().stream().map(
                                s -> s.getWeight() * (36 / (37 - s.getRepetitions()))
                        ).max(Double::compare).get(),
                        we.getSets().stream().map(WorkoutSet::getWeight).max(Double::compare).get()
                )
        ).collect(Collectors.toCollection(ArrayList::new));
    }
}
