package com.sparta.schedulemanagement.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class ScheduleRequestDto {
    int scheduleId;
    int managerId;
    String content;
    String password;
    LocalDateTime createDate;
    LocalDateTime modifyDate;
}
