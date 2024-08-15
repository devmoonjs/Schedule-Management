package com.sparta.schedulemanagement.dto;

import jakarta.validation.constraints.Email;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Getter
@Setter
public class ManagerRequestDto {
    int managerId;
    String name;

    @Email(message = "이메일 형식을 제대로 입력해주세요.")
    String email;

    LocalDateTime registerDate;
    LocalDateTime modifyDate;
}
