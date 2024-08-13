package com.sparta.schedulemanagement.controller;

import com.sparta.schedulemanagement.entity.Manager;
import com.sparta.schedulemanagement.repository.JdbcScheduleRepository;
import com.sparta.schedulemanagement.service.ScheduleService;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/managers")
public class ManagerController {
    private final JdbcTemplate jdbcTemplate;
    private final ScheduleService scheduleService;
    private final JdbcScheduleRepository jdbcScheduleRepository;

    public ManagerController(JdbcTemplate jdbcTemplate, ScheduleService scheduleService, JdbcScheduleRepository jdbcScheduleRepository) {
        this.jdbcTemplate = jdbcTemplate;
        this.scheduleService = scheduleService;
        this.jdbcScheduleRepository = jdbcScheduleRepository;
    }

    //
}
