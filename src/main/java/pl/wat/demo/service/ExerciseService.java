package pl.wat.demo.service;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import pl.wat.demo.dto.ExerciseDataResponse;
import pl.wat.demo.dto.ExerciseDetails;
import pl.wat.demo.dto.ExerciseSearchRequest;
import pl.wat.demo.mapper.ExerciseMapper;
import pl.wat.demo.mapper.WorkoutMapper;
import pl.wat.demo.model.*;
import pl.wat.demo.repository.*;

import java.util.*;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ExerciseService {
    private final ExerciseRepository exerciseRepository;
    private final MuscleRepository musicleRepository;
    private final UserRepository userRepository;
    private final WorkoutExerciseRepository workoutExerciseRepository;
    private final WorkoutMapper workoutMapper;
    private final WorkoutRepository workoutRepository;
    private final ExerciseMapper exerciseMapper;

    public List<Muscle> getMuscles() {
        return musicleRepository.findAll();
    }

    public List<Exercise> getExercises(ExerciseSearchRequest request) {
        if(request.search().isEmpty()){
            return exerciseRepository.findAllByMusclesIdIn(request.muscleIds());
        } else if (request.muscleIds().isEmpty()){
            return exerciseRepository.findAllByNameContainingIgnoreCase(request.search());
        } else {
            return exerciseRepository.findAllByNameContainingIgnoreCaseAndHavingAllMuscles(request.muscleIds(), request.search());
        }
    }

    public Exercise getExerciseById(int id) {
        return exerciseRepository.findById(id).orElse(null);
    }

    public List<Exercise> getRecordedExercises(UUID userId) {
        User user = userRepository.findById(userId).orElseThrow();
        return exerciseRepository.findAllRecordedByUser(user);
    }

    public List<ExerciseDataResponse> getExerciseData(UUID userId, int exerciseId) {
        User user = userRepository.findById(userId).orElseThrow();
        List<WorkoutExercise> records = workoutExerciseRepository.findAllRecords(user, exerciseId);
        return workoutMapper.toExerciseDataList(records);
    }

    public List<Exercise> getLatestExercises(String userId) {
        int maxExercises = 10;
        UUID uuid = UUID.fromString(userId);
        List<Workout> lastWorkouts = workoutRepository.findAllByUserId(uuid, PageRequest.of(0, 10, Sort.by("date").descending())).getContent();
        List<Exercise> exercises = lastWorkouts.stream().flatMap(w -> w.getExercises().stream().map(we -> we.getExercise())).collect(Collectors.toSet()).stream().toList();
        return exercises.subList(0, Math.min(exercises.size(), 10));
    }

    public ExerciseDetails getExerciseDetails(int exerciseId) {
        return exerciseMapper.toExerciseDetails(exerciseRepository.findById(exerciseId).orElseThrow());
    }

}
