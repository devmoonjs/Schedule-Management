package com.sparta.schedulemanagement.controller;

import com.sparta.schedulemanagement.dto.*;
import com.sparta.schedulemanagement.service.ScheduleService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api")
public class ScheduleController {

    private final ScheduleService scheduleService;

    public ScheduleController(ScheduleService scheduleService) {
        this.scheduleService = scheduleService;
    }

    // 일정 생성 (managerId, content, password)
    @PostMapping("/schedules")
    public ResponseEntity<ScheduleResponseDto> createSchedule(@Valid @RequestBody CreateScheduleRequestDto scheduleRequestDto) {
        ScheduleResponseDto responseDto = scheduleService.save(scheduleRequestDto);
        if (responseDto == null) {
            return ResponseEntity.status(404).build();
        }
        return ResponseEntity.status(201).body(responseDto); // 201 Created
    }

    // 단일 일정 조회
    @GetMapping("/schedules/{scheduleId}")
    public ResponseEntity<ScheduleResponseDto> getSchedule(@PathVariable int scheduleId) {
        ScheduleResponseDto responseDto = scheduleService.findById(scheduleId);
        if (responseDto == null) {
            return ResponseEntity.status(404).build();
        }
        return ResponseEntity.ok().body(responseDto); // 200 Ok
    }

    // 일정 리스트 조회 (modifyDate, managerId)
    @GetMapping("/schedules")
    public ResponseEntity<List<ScheduleResponseDto>> getScheduleList(
            @PageableDefault(page = 0, size = 5, sort = "createDate", direction = Sort.Direction.DESC) Pageable pageable,
            @RequestParam(required = false) LocalDate modifyDate,
            @RequestParam(required = false) Integer managerId) {
        List<ScheduleResponseDto> responseDtoList = scheduleService.findAll(modifyDate, managerId, pageable);
        if (responseDtoList ==  null) {
            return ResponseEntity.status(404).build();
        }
        return ResponseEntity.ok().body(responseDtoList); // 200 Ok
    }

    // 일정 내용 변경 (scheduleId, managerId, content, password)
    @PutMapping("/schedules/{scheduleId}")
    public ResponseEntity<ScheduleResponseDto> updateSchedule(@PathVariable int scheduleId, @RequestBody ScheduleRequestDto scheduleRequestDto) {
        ScheduleResponseDto responseDto = scheduleService.update(scheduleId, scheduleRequestDto);
        if (responseDto == null) {
            return ResponseEntity.status(404).build();
        }
        return ResponseEntity.ok().body(responseDto); // 200 Ok
    }

    // 일정 삭제 (password)
    @DeleteMapping("/schedules/{scheduleId}")
    public ResponseEntity<Void> delete(@PathVariable int scheduleId, @RequestBody ScheduleRequestDto scheduleRequestDto) {
        if (scheduleService.delete(scheduleId, scheduleRequestDto)) {
            return ResponseEntity.noContent().build(); // 204 Deleted
        } else {
            return ResponseEntity.status(404).build();
        }
    }

    // 담당자 등록 (name)
    @PostMapping("/managers")
    public ResponseEntity<ManagerResponseDto> createManager(@RequestBody ManagerRequestDto requestDto) {
        ManagerResponseDto responseDto = scheduleService.createManager(requestDto);
        if (responseDto == null) {
            ResponseEntity.status(404).build();
        }
        return ResponseEntity.status(201).body(responseDto); // 201 Created
    }

    // 담당자 정보 등록 (email)
    @PostMapping("/managers/{managerId}")
    public ResponseEntity<ManagerResponseDto> updateManager(@PathVariable int managerId, @Valid @RequestBody ManagerRequestDto requestDto) {
        ManagerResponseDto responseDto = scheduleService.updateManger(managerId, requestDto);
        if (responseDto == null) {
            return ResponseEntity.status(404).build();
        }
        return ResponseEntity.ok().body(responseDto); // 200 Ok
    }

    // 담당자 삭제
    @DeleteMapping("/managers/{managerId}")
    public ResponseEntity<Void> deleteManger(@PathVariable int managerId) {
        if(scheduleService.deleteManger(managerId)) {
            return ResponseEntity.status(204).build();
        }
        return ResponseEntity.status(404).build();
    }
}
