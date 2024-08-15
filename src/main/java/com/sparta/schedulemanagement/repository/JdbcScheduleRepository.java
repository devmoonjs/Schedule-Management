package com.sparta.schedulemanagement.repository;

import com.sparta.schedulemanagement.dto.*;
import com.sparta.schedulemanagement.exception.EntityNotFoundException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.LocalDateTime;
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
    public ScheduleResponseDto save(CreateScheduleRequestDto requestDto) {
        // manager_id 유효성 체크
        findManagerById(requestDto.getManagerId());

        // 생성 및 수정 날짜 설정
        requestDto.setCreateDate(LocalDateTime.now());
        requestDto.setModifyDate(LocalDateTime.now());

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

        return new ScheduleResponseDto(requestDto);
    }

    // id 값으로 일정 찾기
    public Optional<ScheduleResponseDto> findById(int scheduleId) {
        String sql = "SELECT * FROM schedule WHERE schedule_id = ?";
        try {
            ScheduleResponseDto responseDto = jdbcTemplate.queryForObject(sql, new Object[]{scheduleId}, (rs, rowNum) ->
                    new ScheduleResponseDto(
                            rs.getInt("schedule_id"),
                            rs.getInt("manager_id"),
                            rs.getString("content"),
                            rs.getTimestamp("create_date").toLocalDateTime(),
                            rs.getTimestamp("modify_date").toLocalDateTime()
                    )
            );
            return Optional.ofNullable(responseDto);
        } catch (EmptyResultDataAccessException exception) { // 비어있는 객체라면 empty 반환
            return Optional.empty();
        }
    }

    // 일정 전부 조회 - 페이징 처리
    public List<ScheduleResponseDto> findAll(LocalDate modifyDate, Integer managerId, Pageable pageable) {
        String sql = "SELECT * FROM schedule";
        List<Object> param = new ArrayList<>();

        if (modifyDate != null && managerId != null) { // 값이 두개 들어왔을 때
            sql += " WHERE modify_date < ? AND manager_id = ? ORDER BY modify_date DESC";
            param.add(modifyDate);
            param.add(managerId);

        } else if (modifyDate == null && managerId == null) { // 값이 아무것도 없을 때
            sql += " ORDER BY modify_date DESC";

        } else if (modifyDate != null) { // 값이 수정일만 들어왔을 때
            sql += " WHERE modify_date < ? ORDER BY modify_date DESC";
            param.add(modifyDate);

        } else if (managerId != null) { // 값이 담당자명만 들어왔을 때
            sql += " WHERE manager_id = ? ORDER BY modify_date DESC";
            param.add(managerId);
        }

        // 페이징을 위한 LIMIT, OFFSET 설정
        sql += " LIMIT ? OFFSET ?";
        param.add(pageable.getPageSize());
        param.add(pageable.getOffset());

        return jdbcTemplate.query(sql, param.toArray(), (rs, rowNum) -> {
            int scheduleId = rs.getInt("schedule_id");
            int managerIdResult = rs.getInt("manager_id");
            String content = rs.getString("content");
            LocalDate createDate = rs.getTimestamp("create_date").toLocalDateTime().toLocalDate();
            LocalDate modifyDateResult = rs.getTimestamp("modify_date").toLocalDateTime().toLocalDate();
            return new ScheduleResponseDto(scheduleId, managerIdResult, content, createDate.atStartOfDay(), modifyDateResult.atStartOfDay());
        });
    }

    // 일정 수정
    public Optional<ScheduleResponseDto> update(int scheduleId, ScheduleRequestDto requestDto) {

        if (requestDto.getContent() == null && requestDto.getManagerId() == 0) { // 모든 정보가 들어오지 않을 떄
            throw new IllegalArgumentException("수정할 내용이 작성되지 않았습니다.");
        } else if (requestDto.getContent() != null && requestDto.getManagerId() == 0) { // content 만 들어올 때
            if (passwordCheck(scheduleId, requestDto)) {
                String updateSql = "UPDATE schedule SET content = ?, modify_date = ? WHERE schedule_id = ?";
                LocalDateTime modifyDate = LocalDateTime.now();
                jdbcTemplate.update(updateSql, requestDto.getContent(), modifyDate, scheduleId);
                return findById(scheduleId);
            } else {
                throw new EntityNotFoundException("Password is incorrect. 비밀번호를 다시 입력하세요.");
            }
        } else if (requestDto.getContent() == null) { // mangerId 만 들어올 때
            if (passwordCheck(scheduleId, requestDto)) {
                String updateSql = "UPDATE schedule SET manager_id = ?, modify_date = ? WHERE schedule_id = ?";
                LocalDateTime modifyDate = LocalDateTime.now();
                jdbcTemplate.update(updateSql, requestDto.getManagerId(), modifyDate, scheduleId);
                return findById(scheduleId);
            } else {
                throw new EntityNotFoundException("Password is incorrect. 비밀번호를 다시 입력하세요.");
            }
        } else { // content, managerId 두 정보가 들어올 때
            if (passwordCheck(scheduleId, requestDto)) {
                String updateSql = "UPDATE schedule SET manager_id = ?, content = ?, modify_date = ? WHERE schedule_id = ?";
                LocalDateTime modifyDate = LocalDateTime.now();
                jdbcTemplate.update(updateSql, requestDto.getManagerId(), requestDto.getContent(), modifyDate, scheduleId);
                return findById(scheduleId);
            }
        }
        return Optional.empty();
    }

    // 비밀번호 체크
    public boolean passwordCheck(int scheduleId, ScheduleRequestDto requestDto) {
        String sql = "SELECT password FROM schedule WHERE schedule_id = ?";
        String password = jdbcTemplate.queryForObject(sql, String.class, scheduleId);
        return requestDto.getPassword().equals(password);
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
        requestDto.setRegisterDate(LocalDateTime.now());
        requestDto.setModifyDate(LocalDateTime.now());
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
        LocalDateTime modifyDate = LocalDateTime.now();

        jdbcTemplate.update(updateSql, requestDto.getEmail(), modifyDate, managerId);
        return managerFindById(managerId);
    }

    // 담당자 고유번호로 담당자 정보 찾기
    public ManagerResponseDto managerFindById(int managerId) {
        String sql = "SELECT * FROM manager WHERE manager_id = ?";
        return jdbcTemplate.query(sql, resultSet -> {
            if (resultSet.next()) {
                ManagerResponseDto responseDto = new ManagerResponseDto();
                responseDto.setManagerId(resultSet.getInt("manager_id"));
                responseDto.setName(resultSet.getString("name"));
                responseDto.setEmail(resultSet.getString("email"));
                responseDto.setRegisterDate(resultSet.getTimestamp("register_date").toLocalDateTime());
                responseDto.setModifyDate(resultSet.getTimestamp("modify_date").toLocalDateTime());
                return responseDto;
            } else {
                return null;
            }
        }, managerId);
    }

    // 담당자 유효성 체크
    public void findManagerById(int managerId) {
        String findManagerSql = "SELECT COUNT(*) FROM manager WHERE manager_id = ?";
        int count = jdbcTemplate.queryForObject(findManagerSql, new Object[]{managerId}, Integer.class);
        if (count == 0) {
            throw new EntityNotFoundException("Manager Id " + managerId + " not Found. 담당자를 제대로 선택하세요.");
        }
    }

    public boolean deleteManger(int managerId) {
        findManagerById(managerId); // 담당자 유효성 체크
        String deleteSql = "DELETE FROM manager WHERE manager_id = ?";
        jdbcTemplate.update(deleteSql, managerId);

        return true;
    }
}
