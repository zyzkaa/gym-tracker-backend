package pl.wat.demo.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;
import pl.wat.demo.dto.PlanRequest;
import pl.wat.demo.dto.PlanResponse;
import pl.wat.demo.dto.ShortPlanResponse;
import pl.wat.demo.service.PlanService;

import java.util.List;

@RestController
@RequestMapping("/plan")
@AllArgsConstructor
public class PlanController {

    private final PlanService planService;

    @PostMapping("/add")
    public ResponseEntity<Integer> addPlan(@RequestBody PlanRequest planRequest, @AuthenticationPrincipal Jwt jwt) {
        return ResponseEntity.ok(planService.addPlan(planRequest, jwt.getSubject()));
    }   

    @GetMapping("/list")
    public ResponseEntity<List<ShortPlanResponse>> getUserPlans(@AuthenticationPrincipal Jwt jwt) {
        return ResponseEntity.ok(planService.getUsersPlans(jwt.getSubject()));
    }

    @GetMapping("/details/{planId}")
    public ResponseEntity<PlanResponse> getPlanDetails(@PathVariable int planId, @AuthenticationPrincipal Jwt jwt) {
        return ResponseEntity.ok(planService.getPlanDetails(planId, jwt.getSubject()));
    }

    @DeleteMapping("/remove/{planId}")
    public void  deletePlan(@PathVariable int planId, @AuthenticationPrincipal Jwt jwt) {
        planService.deletePlan(planId, jwt.getSubject());
    }
}
