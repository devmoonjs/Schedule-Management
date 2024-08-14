package com.sparta.schedulemanagement.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
public class ManagerRequestDto {
    int managerId;
    String name;
    String email;
    LocalDateTime registerDate;
    LocalDateTime modifyDate;
}
