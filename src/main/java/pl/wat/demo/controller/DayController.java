package pl.wat.demo.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.wat.demo.dto.DayResponse;
import pl.wat.demo.service.DayService;

@RestController
@RequestMapping("/day")
@AllArgsConstructor
public class DayController {
    private final DayService dayService;

    @GetMapping("/{id}")
    public ResponseEntity<DayResponse> getDayById(@PathVariable int id) {
        return ResponseEntity.ok(dayService.getDayById(id));
    }
}
