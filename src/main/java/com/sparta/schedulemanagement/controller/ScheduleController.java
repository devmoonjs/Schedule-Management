package com.sparta.schedulemanagement.controller;
import com.sparta.schedulemanagement.dto.ScheduleRequestDto;
import com.sparta.schedulemanagement.dto.ScheduleResponseDto;
import com.sparta.schedulemanagement.repository.JdbcScheduleRepository;
import com.sparta.schedulemanagement.service.ScheduleService;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

    @PostMapping("/schedules")
    public ScheduleResponseDto createSchedule(@RequestBody ScheduleRequestDto scheduleRequestDto) {
        System.out.println("test");
        ScheduleResponseDto responseDto = scheduleService.save(scheduleRequestDto);
        if(responseDto != null) {
            return responseDto;
        };
        return null;
    }
}
