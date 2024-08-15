package com.sparta.schedulemanagement.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class ManagerResponseDto {
    int managerId;
    String name;
    String email;
    LocalDateTime registerDate;
    LocalDateTime modifyDate;

    public ManagerResponseDto(ManagerRequestDto requestDto) {
        this.managerId = requestDto.getManagerId();
        this.name = requestDto.getName();
        this.registerDate = requestDto.getRegisterDate();
        this.modifyDate = requestDto.getModifyDate();
    }

}


