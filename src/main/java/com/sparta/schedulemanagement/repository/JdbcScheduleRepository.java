package com.sparta.schedulemanagement.repository;

import com.sparta.schedulemanagement.dto.ScheduleRequestDto;
import com.sparta.schedulemanagement.dto.ScheduleResponseDto;
import com.sparta.schedulemanagement.entity.Schedule;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Repository
public class JdbcScheduleRepository {

    private final JdbcTemplate jdbcTemplate;

    public JdbcScheduleRepository(DataSource dataSource) {
        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    // 일정 저장
    public ScheduleResponseDto save(Schedule schedule) {
        schedule.setCreateDate(LocalDate.now());
        schedule.setModifyDate(LocalDate.now());

        // 날짜 변경 테스트
//        schedule.setCreateDate(LocalDate.of(2023,6,20));
//        schedule.setModifyDate(LocalDate.of(2023,6,20));


        String sql = "INSERT INTO schedule(name, password, content, create_date, modify_date) VALUES(?, ?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder(); // 기본 키를 반환받기 위한 객체

        jdbcTemplate.update(con -> {
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

    // id 값으로 일정 찾기
    public ScheduleResponseDto findById(int scheduleId) {
        String sql = "SELECT * FROM schedule WHERE id = ?";
        return jdbcTemplate.query(sql, resultSet -> {
            if (resultSet.next()) {
                ScheduleResponseDto responseDto = new ScheduleResponseDto();
                responseDto.setId(resultSet.getInt("id"));
                responseDto.setName(resultSet.getString("name"));
                responseDto.setContent(resultSet.getString("content"));
                responseDto.setCreateDate(resultSet.getTimestamp("create_date").toLocalDateTime().toLocalDate());
                responseDto.setModifyDate(resultSet.getTimestamp("modify_date").toLocalDateTime().toLocalDate());
                return responseDto;
            } else {
                return null;
            }
        }, scheduleId);
    }

    // 일정 전부 조회
    public List<ScheduleResponseDto> findAll(LocalDate modifyDate, String name) {
        String sql = "SELECT * FROM schedule ";
        List<Object> param = new ArrayList<>();

        // 값이 두개 들어왔을 때
        if (modifyDate != null && name != null) {
            sql += "WHERE modify_date < ? AND name = ? ORDER BY modify_date DESC";
            param.add(modifyDate);
            param.add(name);

        // 값이 날짜만 들어왔을 때
        } else if (modifyDate != null) {
            sql += "WHERE modify_date < ? ORDER BY modify_date DESC";
            param.add(modifyDate);

        // 값이 담당자명만 들어왔을 때
        } else if (name != null) {
            sql += "WHERE name = ? ORDER BY modify_date DESC";
            param.add(name);
        }

        return jdbcTemplate.query(sql, new RowMapper<ScheduleResponseDto>() {
            public ScheduleResponseDto mapRow(ResultSet rs, int rowNum) throws SQLException {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                String content = rs.getString("content");
                LocalDate createDate = rs.getTimestamp("create_date").toLocalDateTime().toLocalDate();
                LocalDate modifyDate = rs.getTimestamp("modify_date").toLocalDateTime().toLocalDate();
                return new ScheduleResponseDto(id, name, content, createDate, modifyDate);
            }
        } ,param.toArray());
    }

    // 일정 수정
    public ScheduleResponseDto update(int id, ScheduleRequestDto requestDto) {
        String sql = "SELECT password FROM schedule WHERE id = ?";
        String password = jdbcTemplate.queryForObject(sql, String.class, id);
        if (password.equals(requestDto.getPassword())) {
            String updateSql = "UPDATE schedule SET name = ?, content = ?, modify_date = ? WHERE id = ?";
            LocalDate modifyDate = LocalDate.now();
//            LocalDate modifyDateTest = LocalDate.of(2025,12,25); // 임의로 지정한 날짜 테스트
            jdbcTemplate.update(updateSql, requestDto.getName(), requestDto.getContent(), modifyDate, id);
            return findById(id);
        } else {
            return null;
        }
    }

    public boolean delete(int id, ScheduleRequestDto requestDto) {
        String sql = "SELECT password FROM schedule WHERE id = ?";
        String password = jdbcTemplate.queryForObject(sql, String.class, id);
        if (password.equals(requestDto.getPassword())) {
            String deleteSql = "DELETE FROM schedule WHERE id = ?";
            jdbcTemplate.update(deleteSql, id);
            return true;
        } else {
            return false;
        }
    }
}
