package pl.wat.demo.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import pl.wat.demo.dto.ExerciseDataResponse;
import pl.wat.demo.dto.ExerciseSearchRequest;
import pl.wat.demo.mapper.WorkoutMapper;
import pl.wat.demo.model.Exercise;
import pl.wat.demo.model.Muscle;
import pl.wat.demo.model.User;
import pl.wat.demo.model.WorkoutExercise;
import pl.wat.demo.repository.ExerciseRepository;
import pl.wat.demo.repository.MuscleRepository;
import pl.wat.demo.repository.UserRepository;
import pl.wat.demo.repository.WorkoutExerciseRepository;

import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public class ExerciseService {
    private final ExerciseRepository exerciseRepository;
    private final MuscleRepository musicleRepository;
    private final UserRepository userRepository;
    private final WorkoutExerciseRepository workoutExerciseRepository;
    private final WorkoutMapper workoutMapper;

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

}
