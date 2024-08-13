package com.sparta.schedulemanagement.controller;
import com.sparta.schedulemanagement.dto.ScheduleRequestDto;
import com.sparta.schedulemanagement.dto.ScheduleResponseDto;
import com.sparta.schedulemanagement.entity.Schedule;
import com.sparta.schedulemanagement.repository.JdbcScheduleRepository;
import com.sparta.schedulemanagement.service.ScheduleService;
import lombok.Getter;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api")
public class ScheduleController {

    private final JdbcTemplate jdbcTemplate;
    private final ScheduleService scheduleService;
    private final JdbcScheduleRepository jdbcScheduleRepository;

    public ScheduleController(JdbcTemplate jdbcTemplate, ScheduleService scheduleService, JdbcScheduleRepository jdbcScheduleRepository) {
        this.jdbcTemplate = jdbcTemplate;
        this.scheduleService = scheduleService;
        this.jdbcScheduleRepository = jdbcScheduleRepository;
    }

    // 일정 생성
    @PostMapping("/schedules")
    public ScheduleResponseDto createSchedule(@RequestBody ScheduleRequestDto scheduleRequestDto) {
        ScheduleResponseDto responseDto = scheduleService.save(scheduleRequestDto);
        if (responseDto != null) {
            return responseDto;
        }
        ;
        return null;
    }

    // 단일 일정 조회
    @GetMapping("/schedules/{scheduleId}")
    public ScheduleResponseDto getSchedule(@PathVariable int scheduleId) {
        return scheduleService.findById(scheduleId);
    }

    // 일정 리스트 조회
    @GetMapping("/schedules")
    public List<ScheduleResponseDto> getScheduleList(@RequestParam (required = false) LocalDate modifyDate, @RequestParam (required = false) String name) {
        return scheduleService.findAll(modifyDate, name);
    }

    // 일정 내용 변경
    @PostMapping("/schedules/{scheduleId}")
    public ScheduleResponseDto updateSchedule(@PathVariable int scheduleId, @RequestBody ScheduleRequestDto scheduleRequestDto) {
        return scheduleService.update(scheduleId, scheduleRequestDto);
    }

    // 일정 삭제
    @DeleteMapping("/schedules/{scheduleId}")
    public ResponseEntity<String> delete(@PathVariable int scheduleId, @RequestBody ScheduleRequestDto scheduleRequestDto) {
        if (scheduleService.delete(scheduleId, scheduleRequestDto)) {
            return ResponseEntity.ok("Deleted Successfully");
        } else {
            return ResponseEntity.status(404).body("Schedule Not Found");
        }
    }
}
