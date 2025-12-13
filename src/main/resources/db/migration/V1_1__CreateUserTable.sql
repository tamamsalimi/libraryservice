-- V1_1__CreateUserTable.sql
CREATE TABLE users
(
    id        BIGSERIAL PRIMARY KEY,
    username  VARCHAR(255) NOT NULL UNIQUE,
    password  VARCHAR(255) NOT NULL,
    role      VARCHAR(20)  NOT NULL,
    member_id BIGINT UNIQUE,
    CONSTRAINT fk_user_member
        FOREIGN KEY (member_id) REFERENCES members (id)
);
