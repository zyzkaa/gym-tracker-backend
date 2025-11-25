package pl.wat.demo.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pl.wat.demo.model.Exercise;
import pl.wat.demo.model.User;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

@Repository
public interface ExerciseRepository extends JpaRepository<Exercise, Integer> {
    @Query("""
    select e
    from Exercise e
      join e.muscles m
    where m.id in :ids
    group by e
    having count(distinct m.id) = :#{#ids.size()}
  """)
    List<Exercise> findAllByMusclesIdIn(@Param("ids") Collection<Integer> ids);

    List<Exercise> findAllByNameContainingIgnoreCase(String search);

    @Query("""
    select e
    from Exercise e
      join e.muscles m
    where lower(e.name) like lower(concat('%', :name, '%'))
      and m.id in :ids
    group by e
    having count(distinct m.id) = :#{#ids.size()}
  """)
    List<Exercise> findAllByNameContainingIgnoreCaseAndHavingAllMuscles(
            @Param("ids") Collection<Integer> ids,
            @Param("name") String name);

    @Query("""
    select ex
    from Workout w
    join w.exercises we
    join we.exercise ex
    where w.user = :user
    group by ex
  """)
    List<Exercise> findAllRecordedByUser(
            @Param("user") User user);

}
