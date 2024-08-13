package com.sparta.schedulemanagement.repository;

import com.sparta.schedulemanagement.dto.ManagerRequestDto;
import com.sparta.schedulemanagement.dto.ManagerResponseDto;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Repository
public class JdbcScheduleRepository {

    private final JdbcTemplate jdbcTemplate;

    public JdbcScheduleRepository(DataSource dataSource) {
        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    // 일정 저장
    public ScheduleResponseDto save(ScheduleRequestDto requestDto) {
        requestDto.setCreateDate(LocalDate.now());
        requestDto.setModifyDate(LocalDate.now());

        // 날짜 변경 테스트
//        schedule.setCreateDate(LocalDate.of(2023,6,20));
//        schedule.setModifyDate(LocalDate.of(2023,6,20));


        String sql = "INSERT INTO schedule(manager_id, password, content, create_date, modify_date) VALUES(?, ?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder(); // 기본 키를 반환받기 위한 객체

        jdbcTemplate.update(con -> {
                    PreparedStatement preparedStatement = con.prepareStatement(sql,
                            Statement.RETURN_GENERATED_KEYS);

                    preparedStatement.setInt(1, requestDto.getManagerId());
                    preparedStatement.setString(2, requestDto.getPassword());
                    preparedStatement.setString(3, requestDto.getContent());
                    preparedStatement.setObject(4, requestDto.getCreateDate());
                    preparedStatement.setObject(5, requestDto.getModifyDate());
                    return preparedStatement;
                },
                keyHolder);

        requestDto.setScheduleId(Objects.requireNonNull(keyHolder.getKey()).intValue());

        ScheduleResponseDto responseDto = new ScheduleResponseDto(requestDto);

        return responseDto;
    }

    // id 값으로 일정 찾기
    public ScheduleResponseDto findById(int scheduleId) {
        String sql = "SELECT * FROM schedule WHERE schedule_id = ?";
        return jdbcTemplate.query(sql, resultSet -> {
            if (resultSet.next()) {
                ScheduleResponseDto responseDto = new ScheduleResponseDto();
                responseDto.setScheduleId(resultSet.getInt("schedule_id"));
                responseDto.setManagerId(resultSet.getInt("manager_id"));
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
    public List<ScheduleResponseDto> findAll(LocalDate modifyDate, int managerId) {
        String sql = "SELECT * FROM schedule ";
        List<Object> param = new ArrayList<>();

        // 값이 두개 들어왔을 때
        if (modifyDate != null && managerId != 0) {
            sql += "WHERE modify_date < ? AND manager_id = ? ORDER BY modify_date DESC";
            param.add(modifyDate);
            param.add(managerId);

            // 값이 날짜만 들어왔을 때
        } else if (modifyDate != null) {
            sql += "WHERE modify_date < ? ORDER BY modify_date DESC";
            param.add(modifyDate);

            // 값이 담당자명만 들어왔을 때
        } else if (managerId != 0) {
            sql += "WHERE manager_id = ? ORDER BY modify_date DESC";
            param.add(managerId);
        }

        return jdbcTemplate.query(sql, new RowMapper<ScheduleResponseDto>() {
            public ScheduleResponseDto mapRow(ResultSet rs, int rowNum) throws SQLException {
                int scheduleId = rs.getInt("schedule_id");
                int managerId = rs.getInt("manager_id");
                String content = rs.getString("content");
                LocalDate createDate = rs.getTimestamp("create_date").toLocalDateTime().toLocalDate();
                LocalDate modifyDate = rs.getTimestamp("modify_date").toLocalDateTime().toLocalDate();
                return new ScheduleResponseDto(scheduleId, managerId, content, createDate, modifyDate);
            }
        }, param.toArray());
    }

    // 일정 수정
    public ScheduleResponseDto update(int scheduleId, ScheduleRequestDto requestDto) {
        String sql = "SELECT password FROM schedule WHERE schedule_id = ?";
        String password = jdbcTemplate.queryForObject(sql, String.class, scheduleId);
        if (password.equals(requestDto.getPassword())) {
            String updateSql = "UPDATE schedule SET manager_id = ?, content = ?, modify_date = ? WHERE schedule_id = ?";
            LocalDate modifyDate = LocalDate.now();
//            LocalDate modifyDateTest = LocalDate.of(2025,12,25); // 임의로 지정한 날짜 테스트
            jdbcTemplate.update(updateSql, requestDto.getManagerId(), requestDto.getContent(), modifyDate, scheduleId);
            return findById(scheduleId);
        } else {
            return null;
        }
    }

    // 일정 삭제
    public boolean delete(int id, ScheduleRequestDto requestDto) {
        String sql = "SELECT password FROM schedule WHERE schedule_id = ?";
        String password = jdbcTemplate.queryForObject(sql, String.class, id);
        if (password.equals(requestDto.getPassword())) {
            String deleteSql = "DELETE FROM schedule WHERE schedule_id = ?";
            jdbcTemplate.update(deleteSql, id);
            return true;
        } else {
            return false;
        }
    }

    // 담당자 생성
    public ManagerResponseDto createManager(ManagerRequestDto requestDto) {
        requestDto.setRegisterDate(LocalDate.now());
        requestDto.setModifyDate(LocalDate.now());
        String sql = "INSERT INTO manager (name, register_date, modify_date) values (?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder(); // 기본 키를 반환받기 위한 객체

        jdbcTemplate.update(con -> {
                    PreparedStatement preparedStatement = con.prepareStatement(sql,
                            Statement.RETURN_GENERATED_KEYS);

                    preparedStatement.setString(1, requestDto.getName());
                    preparedStatement.setObject(2, requestDto.getRegisterDate());
                    preparedStatement.setObject(3, requestDto.getModifyDate());
                    return preparedStatement;
                },
                keyHolder);

        requestDto.setManagerId(Objects.requireNonNull(keyHolder.getKey()).intValue());

        return new ManagerResponseDto(requestDto);
    }

    // 담당자 정보 입력 (이메일)
    public ManagerResponseDto updateManger(int managerId, ManagerRequestDto requestDto) {
        String updateSql = "UPDATE manager SET email = ?, modify_date = ? WHERE manager_id = ?";
        LocalDate modifyDate = LocalDate.now();

        jdbcTemplate.update(updateSql, requestDto.getEmail(), modifyDate, managerId);
        return managerFindById(managerId);
    }

    // 담당자 고유번호로 정보 찾기
    public ManagerResponseDto managerFindById(int managerId) {
        String sql = "SELECT * FROM manager WHERE manager_id = ?";
        return jdbcTemplate.query(sql, resultSet -> {
            if (resultSet.next()) {
                ManagerResponseDto responseDto = new ManagerResponseDto();
                responseDto.setManagerId(resultSet.getInt("manager_id"));
                responseDto.setName(resultSet.getString("name"));
                responseDto.setEmail(resultSet.getString("email"));
                responseDto.setRegisterDate(resultSet.getTimestamp("register_date").toLocalDateTime().toLocalDate());
                responseDto.setModifyDate(resultSet.getTimestamp("modify_date").toLocalDateTime().toLocalDate());
                return responseDto;
            } else {
                return null;
            }
        }, managerId);
    }
}
