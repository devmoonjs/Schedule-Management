package com.sparta.schedulemanagement.service;

import com.sparta.schedulemanagement.dto.ScheduleRequestDto;
import com.sparta.schedulemanagement.dto.ScheduleResponseDto;
import com.sparta.schedulemanagement.entity.Schedule;
import com.sparta.schedulemanagement.repository.JdbcScheduleRepository;
import org.springframework.stereotype.Service;

@Service
public class ScheduleService {

    private final JdbcScheduleRepository scheduleRepository;

    public ScheduleService(JdbcScheduleRepository scheduleRepository) {
        this.scheduleRepository = scheduleRepository;
    }

    public ScheduleResponseDto save(ScheduleRequestDto scheduleRequestDto) {
        Schedule schedule = new Schedule(scheduleRequestDto);
        ScheduleResponseDto responseDto = scheduleRepository.save(schedule);
        return responseDto;
    }
}
