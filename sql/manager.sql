create table manager
(
    manager_id    int auto_increment comment '담당자 고유번호' primary key,
    name          varchar(255) not null comment '이름',
    email         varchar(255) null comment '이메일',
    register_date datetime         not null comment '등록일',
    modify_date   datetime         not null comment '수정일'
);