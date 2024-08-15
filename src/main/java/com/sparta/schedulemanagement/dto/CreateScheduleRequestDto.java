package com.sparta.schedulemanagement.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Getter
@Setter
public class CreateScheduleRequestDto {
    @NotBlank(message = "비밀번호는 필수 입력 값입니다.")
    private String password;

    private int scheduleId;

    private int managerId;

    @Size(max = 200, message = "최대 입력 값은 200 입니다.")
    private String content;

    private LocalDateTime createDate;

    private LocalDateTime modifyDate;
}
