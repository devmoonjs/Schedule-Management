package com.sparta.schedulemanagement.dto;

import com.sparta.schedulemanagement.entity.Schedule;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
public class ScheduleResponseDto {
    int id;
    String content;
    String name;
    LocalDate createDate;
    LocalDate modifyDate;

    public ScheduleResponseDto(Schedule schedule) {
        this.id = schedule.getId();
        this.content = schedule.getContent();
        this.name = schedule.getName();
        this.createDate = schedule.getCreateDate();
        this.modifyDate = schedule.getModifyDate();
    }

    public ScheduleResponseDto(int id, String content, String name, LocalDate createDate, LocalDate modifyDate) {
        this.id = id;
        this.content = content;
        this.name = name;
        this.createDate = createDate;
        this.modifyDate = modifyDate;
    }
}
