package com.sparta.schedulemanagement.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class ScheduleRequestDto {
    int scheduleId;
    int managerId;
    String content;
    String password;
    LocalDate createDate;
    LocalDate modifyDate;

}
