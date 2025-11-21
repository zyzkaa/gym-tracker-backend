package pl.wat.demo.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;
import pl.wat.demo.dto.ShortWorkoutResponse;
import pl.wat.demo.dto.WorkoutRequest;
import pl.wat.demo.dto.WorkoutResponse;
import pl.wat.demo.dto.WorkoutsPaginationResponse;
import pl.wat.demo.service.WorkoutService;

import java.util.List;

@RestController
@RequestMapping("/workout")
@AllArgsConstructor
public class WorkoutController {
    private final WorkoutService workoutService;

    @GetMapping("history/{amount}/{offset}")
    public ResponseEntity<WorkoutsPaginationResponse> getTrainingHistory(
            @AuthenticationPrincipal Jwt jwt, @PathVariable int amount, @PathVariable int offset) {
        return ResponseEntity.ok(workoutService.getWorkoutsHistory(amount, offset, jwt.getSubject()));
    }

    @PostMapping("/add")
    public ResponseEntity<Integer> addWorkout(
            @AuthenticationPrincipal Jwt jwt, @RequestBody WorkoutRequest workoutRequest) {
        return ResponseEntity.ok(workoutService.addWorkout(jwt.getSubject(), workoutRequest));
    }

    @GetMapping("/{id}")
    public ResponseEntity<WorkoutResponse> getWorkout(@PathVariable int id) {
        return ResponseEntity.ok(workoutService.getById(id));
    }
}
