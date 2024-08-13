package com.sparta.schedulemanagement.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
public class Manager {
    int managerId;
    String name;
    String email;
    LocalDate registerDate;
    LocalDate modifyDate;
}
