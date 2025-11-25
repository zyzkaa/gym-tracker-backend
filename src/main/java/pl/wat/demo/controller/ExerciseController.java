package pl.wat.demo.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;
import pl.wat.demo.dto.ExerciseDataResponse;
import pl.wat.demo.dto.ExerciseDetails;
import pl.wat.demo.dto.ExerciseSearchRequest;
import pl.wat.demo.model.Exercise;
import pl.wat.demo.model.Muscle;
import pl.wat.demo.service.ExerciseService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/exercise")
@AllArgsConstructor
public class ExerciseController {
    private ExerciseService exerciseService;

    @GetMapping("/muscles")
    public ResponseEntity<List<Muscle>> getMuscles() {
        return ResponseEntity.ok(exerciseService.getMuscles());
    }

    @PostMapping("/search")
    public ResponseEntity<List<Exercise>> searchExercise(@RequestBody ExerciseSearchRequest request) {
        return ResponseEntity.ok(exerciseService.getExercises(request));
    }

    @GetMapping("/recorded")
    public ResponseEntity<List<Exercise>> getRecordedExercises(@AuthenticationPrincipal Jwt jwt) {
        return ResponseEntity.ok(exerciseService.getRecordedExercises(UUID.fromString(jwt.getSubject())));
    }

    @GetMapping("/recorded/{id}")
    public ResponseEntity<List<ExerciseDataResponse>> getExerciseRecords(@PathVariable int id, @AuthenticationPrincipal Jwt jwt) {
        return ResponseEntity.ok(exerciseService.getExerciseData(UUID.fromString(jwt.getSubject()), id));
    }

    @GetMapping("/latest")
    public ResponseEntity<List<Exercise>> getLatestExercises(@AuthenticationPrincipal Jwt jwt){
        return ResponseEntity.ok(exerciseService.getLatestExercises(jwt.getSubject()));
    }

    @GetMapping("/details/{id}")
    public ResponseEntity<ExerciseDetails> getExerciseDetails(@PathVariable int id) {
        return ResponseEntity.ok(exerciseService.getExerciseDetails(id));
    }


}
