package com.sparta.schedulemanagement.controller;
import com.sparta.schedulemanagement.dto.ManagerRequestDto;
import com.sparta.schedulemanagement.dto.ManagerResponseDto;
import com.sparta.schedulemanagement.dto.ScheduleRequestDto;
import com.sparta.schedulemanagement.dto.ScheduleResponseDto;
import com.sparta.schedulemanagement.repository.JdbcScheduleRepository;
import com.sparta.schedulemanagement.service.ScheduleService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
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

    // 일정 생성 (managerId, content, password)
    @PostMapping("/schedules")
    public ScheduleResponseDto createSchedule(@RequestBody ScheduleRequestDto scheduleRequestDto) {
        return scheduleService.save(scheduleRequestDto);
    }

    // 단일 일정 조회
    @GetMapping("/schedules/{scheduleId}")
    public ScheduleResponseDto getSchedule(@PathVariable int scheduleId) {
        return scheduleService.findById(scheduleId);
    }

    // 일정 리스트 조회
    @GetMapping("/schedules")
    public List<ScheduleResponseDto> getScheduleList(
            @PageableDefault(page = 0, size = 5, sort = "createDate", direction = Sort.Direction.DESC) Pageable pageable,
            @RequestParam (required = false) LocalDate modifyDate,
            @RequestParam (required = false) Integer managerId) {
        return scheduleService.findAll(modifyDate, managerId, pageable);
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

    // 담당자 등록
    @PostMapping("/managers")
    public ManagerResponseDto createManager(@RequestBody ManagerRequestDto requestDto) {
        return scheduleService.createManager(requestDto);
    }

    // 담당자 정보 등록
    @PostMapping("/managers/{managerId}")
    public ManagerResponseDto updateManager(@PathVariable int managerId, @RequestBody ManagerRequestDto requestDto) {
        return scheduleService.updateManger(managerId, requestDto);
    }
}
