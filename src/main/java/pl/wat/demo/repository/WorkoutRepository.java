package pl.wat.demo.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import pl.wat.demo.model.Workout;

import java.util.List;
import java.util.UUID;

public interface WorkoutRepository extends JpaRepository<Workout, Integer> {
    Page<Workout> findAllByUserId(UUID userId, Pageable pageable);
    List<Workout> findAllByUserId(UUID userId);
}
