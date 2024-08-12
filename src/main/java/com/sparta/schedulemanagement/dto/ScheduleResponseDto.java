package com.sparta.schedulemanagement.dto;

import com.sparta.schedulemanagement.entity.Schedule;
import lombok.Getter;

import java.time.LocalDate;

@Getter
public class ScheduleResponseDto {
    int id;
    String content;
    String name;
    String password;
    LocalDate createDate;
    LocalDate modifyDate;

    public ScheduleResponseDto(Schedule schedule) {
        this.id = schedule.getId();
        this.content = schedule.getContent();
        this.name = schedule.getName();
        this.password = schedule.getPassword();
        this.createDate = schedule.getCreateDate();
        this.modifyDate = schedule.getModifyDate();
    }
}
