create table schedule
(
    schedule_id int auto_increment primary key,
    manager_id  int          not null comment '담당자 고유번호',
    password    varchar(255) not null comment '비밀번호',
    content     varchar(500) not null comment '일정 내용',
    create_date datetime,
    modify_date datetime,
    constraint fk_manager
    foreign key (manager_id) references manager (manager_id)
);