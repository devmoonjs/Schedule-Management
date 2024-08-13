package com.sparta.schedulemanagement.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
public class ManagerResponseDto {
    int managerId;
    String name;
    String email;
    LocalDate registerDate;
    LocalDate modifyDate;

    public ManagerResponseDto(ManagerRequestDto requestDto) {
        this.managerId = requestDto.getManagerId();
        this.name = requestDto.getName();
        this.registerDate = requestDto.getRegisterDate();
        this.modifyDate = requestDto.getModifyDate();
    }

}


