package com.sparta.schedulemanagement.dto;

import com.sparta.schedulemanagement.entity.Schedule;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class ScheduleResponseDto {
    int scheduleId;
    int managerId;
    String content;
    LocalDateTime createDate;
    LocalDateTime modifyDate;

    public ScheduleResponseDto(int id, int managerId, String content, LocalDateTime createDate, LocalDateTime modifyDate) {
        this.scheduleId = id;
        this.managerId = managerId;
        this.content = content;
        this.createDate = createDate;
        this.modifyDate = modifyDate;
    }

    public ScheduleResponseDto(ScheduleRequestDto requestDto) {
        this.scheduleId = requestDto.getScheduleId();
        this.managerId = requestDto.getManagerId();
        this.content = requestDto.getContent();
        this.createDate = requestDto.getCreateDate();
        this.modifyDate = requestDto.getModifyDate();
    }
}
