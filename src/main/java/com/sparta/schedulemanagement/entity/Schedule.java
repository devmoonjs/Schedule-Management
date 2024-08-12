package com.sparta.schedulemanagement.entity;

import com.sparta.schedulemanagement.dto.ScheduleRequestDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDate;


@Getter
@Setter
@NoArgsConstructor
public class Schedule {
    int id;
    String content;
    String name;
    String password;
    LocalDate createDate;
    LocalDate modifyDate;

    public Schedule(ScheduleRequestDto scheduleRequestDto) {
        this.content = scheduleRequestDto.getContent();
        this.name = scheduleRequestDto.getName();
        this.password = scheduleRequestDto.getPassword();
    }
}
