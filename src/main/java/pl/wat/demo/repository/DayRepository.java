package pl.wat.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.wat.demo.model.Day;

@Repository
public interface DayRepository extends JpaRepository<Day, Integer> {
}
