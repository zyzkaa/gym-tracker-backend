package pl.wat.demo.repository;

import jakarta.validation.constraints.NotBlank;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.wat.demo.dto.ShortPlanResponse;
import pl.wat.demo.model.Plan;

import java.util.List;
import java.util.UUID;

@Repository
public interface PlanRepository extends JpaRepository<Plan, Integer> {
    List<Plan> findPlansByAuthorId(UUID authorId);

    List<Plan> findAllByNameContainingIgnoreCase(@NotBlank String name);

    List<Plan> findAllByIsPublicTrueAndNameContainingIgnoreCase(@NotBlank String name);
}
