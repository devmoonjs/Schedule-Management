package com.sparta.schedulemanagement.repository;

import com.sparta.schedulemanagement.dto.ScheduleResponseDto;
import com.sparta.schedulemanagement.entity.Schedule;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Repository
public class JdbcScheduleRepository{

    private final JdbcTemplate jdbcTemplate;

    public JdbcScheduleRepository(DataSource dataSource) {
        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public ScheduleResponseDto save(Schedule schedule) {
        schedule.setCreateDate(LocalDate.now());
        schedule.setModifyDate(LocalDate.now());

        String sql = "INSERT INTO schedule(name, password, content, create_date, modify_date) VALUES(?, ?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder(); // 기본 키를 반환받기 위한 객체

        jdbcTemplate.update( con -> {
                    PreparedStatement preparedStatement = con.prepareStatement(sql,
                            Statement.RETURN_GENERATED_KEYS);

                    preparedStatement.setString(1, schedule.getName());
                    preparedStatement.setString(2, schedule.getPassword());
                    preparedStatement.setString(3, schedule.getContent());
                    preparedStatement.setObject(4, schedule.getCreateDate());
                    preparedStatement.setObject(5, schedule.getModifyDate());
                    return preparedStatement;
                },
                keyHolder);

        schedule.setId(Objects.requireNonNull(keyHolder.getKey()).intValue());

        ScheduleResponseDto responseDto = new ScheduleResponseDto(schedule);

        return responseDto;
    }

    public Optional<Schedule> findById(Long id) {
        return Optional.empty();
    }

    public List<Schedule> findAll() {
        return List.of();
    }
}
