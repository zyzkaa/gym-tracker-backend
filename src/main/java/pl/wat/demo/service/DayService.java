package pl.wat.demo.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import pl.wat.demo.dto.DayResponse;
import pl.wat.demo.mapper.DayMapper;
import pl.wat.demo.model.Day;
import pl.wat.demo.repository.DayRepository;

@Service
@AllArgsConstructor
public class DayService {
    private final DayRepository dayRepository;
    private final DayMapper dayMapper;

    public DayResponse getDayById(int id) {
        Day day = dayRepository.findById(id).orElse(null);
        return (day == null) ? null : dayMapper.toResponse(day);
    }
}
