package pl.wat.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import pl.wat.demo.model.User;
import pl.wat.demo.model.WorkoutExercise;

import java.util.List;

public interface WorkoutExerciseRepository extends JpaRepository<WorkoutExercise, Integer> {
    @Query("""
    select we
    from Workout w
    join w.exercises we
    where we.exercise.id = :exerciseId
    and w.user = :user
""")
    public List<WorkoutExercise> findAllRecords(@Param("user") User user, @Param("exerciseId") int exerciseId);
}
