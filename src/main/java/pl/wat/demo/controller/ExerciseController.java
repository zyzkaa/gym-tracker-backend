package pl.wat.demo.controller;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import pl.wat.demo.dto.ExerciseDataResponse;
import pl.wat.demo.dto.ExerciseDetails;
import pl.wat.demo.dto.ExerciseRequest;
import pl.wat.demo.dto.ExerciseSearchRequest;
import pl.wat.demo.model.Exercise;
import pl.wat.demo.model.Muscle;
import pl.wat.demo.service.ExerciseService;

import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;
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

    @PostMapping(value = "/add", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public void addExercise(@AuthenticationPrincipal Jwt jwt,
                            @RequestPart("body") ExerciseRequest request, @RequestPart(name = "image", required = false) MultipartFile image){
        exerciseService.addExercise(request, image, jwt);
    }

    @GetMapping("/image/{filename}")
    public ResponseEntity<Resource> getImage(@PathVariable String filename){
        Path path = Paths.get("uploads/" + filename);
        try{
            Resource resource = new UrlResource(path.toUri());
            return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(resource);
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }

    @PutMapping(value = "{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public void editExercsie(@AuthenticationPrincipal Jwt jwt, @PathVariable int id,
                             @RequestPart("body") ExerciseRequest request, @RequestPart(name = "image", required = false) MultipartFile image){
        exerciseService.editExercise(id, jwt, request, image);
    }

}
