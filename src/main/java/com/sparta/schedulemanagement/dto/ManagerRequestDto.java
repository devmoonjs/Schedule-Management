package com.sparta.schedulemanagement.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class ManagerRequestDto {
    int managerId;
    String name;
    String email;
    LocalDate registerDate;
    LocalDate modifyDate;
}
