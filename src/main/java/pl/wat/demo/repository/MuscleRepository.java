package pl.wat.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.wat.demo.model.Muscle;

@Repository
public interface MuscleRepository extends JpaRepository<Muscle, Integer> {
}
