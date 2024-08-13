package com.sparta.schedulemanagement.service;

import com.sparta.schedulemanagement.dto.ScheduleRequestDto;
import com.sparta.schedulemanagement.dto.ScheduleResponseDto;
import com.sparta.schedulemanagement.entity.Schedule;
import com.sparta.schedulemanagement.repository.JdbcScheduleRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class ScheduleService {

    private final JdbcScheduleRepository scheduleRepository;

    public ScheduleService(JdbcScheduleRepository scheduleRepository) {
        this.scheduleRepository = scheduleRepository;
    }

    public ScheduleResponseDto save(ScheduleRequestDto scheduleRequestDto) {
        Schedule schedule = new Schedule(scheduleRequestDto);
        return scheduleRepository.save(schedule);
    }

    public ScheduleResponseDto findById(int scheduleId) {
        return scheduleRepository.findById(scheduleId);
    }

    public List<ScheduleResponseDto> findAll(LocalDate modifyDate, String name) {
        return scheduleRepository.findAll(modifyDate, name);
    }

    public ScheduleResponseDto update(int id, ScheduleRequestDto requestDto) {
        return scheduleRepository.update(id, requestDto);
    }

    public boolean delete(int id, ScheduleRequestDto requestDto) {
        return scheduleRepository.delete(id, requestDto);
    }
}
