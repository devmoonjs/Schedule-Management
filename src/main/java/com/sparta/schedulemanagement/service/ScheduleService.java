package com.sparta.schedulemanagement.service;

import com.sparta.schedulemanagement.dto.ManagerRequestDto;
import com.sparta.schedulemanagement.dto.ManagerResponseDto;
import com.sparta.schedulemanagement.dto.ScheduleRequestDto;
import com.sparta.schedulemanagement.dto.ScheduleResponseDto;
import com.sparta.schedulemanagement.entity.Manager;
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
//        Schedule schedule = new Schedule(scheduleRequestDto);
        return scheduleRepository.save(scheduleRequestDto);
    }

    public ScheduleResponseDto findById(int scheduleId) {
        return scheduleRepository.findById(scheduleId);
    }

    public List<ScheduleResponseDto> findAll(LocalDate modifyDate, int managerId) {
        return scheduleRepository.findAll(modifyDate, managerId);
    }

    public ScheduleResponseDto update(int scheduleId, ScheduleRequestDto requestDto) {
        return scheduleRepository.update(scheduleId, requestDto);
    }

    public boolean delete(int id, ScheduleRequestDto requestDto) {
        return scheduleRepository.delete(id, requestDto);
    }

    public ManagerResponseDto createManager(ManagerRequestDto requestDto) {
        return scheduleRepository.createManager(requestDto);
    }

    public ManagerResponseDto updateManger(int managerId, ManagerRequestDto requestDto) {
        return scheduleRepository.updateManger(managerId, requestDto);
    }
}
