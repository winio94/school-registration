CREATE TABLE STUDENT (
ID BIGINT NOT NULL,
UUID VARCHAR(36) NOT NULL,
FIRST_NAME VARCHAR(255) NOT NULL,
LAST_NAME VARCHAR(255) NOT NULL,
PRIMARY KEY (ID));
ALTER TABLE STUDENT ADD CONSTRAINT STUDENT_UUID_UNIQUE UNIQUE (UUID);

CREATE TABLE COURSE (
ID BIGINT NOT NULL,
UUID VARCHAR(36) NOT NULL,
NAME VARCHAR(255) NOT NULL,
CODE VARCHAR(10) NOT NULL,
PRIMARY KEY (ID));
ALTER TABLE COURSE ADD CONSTRAINT COURSE_UUID_UNIQUE UNIQUE (UUID);

CREATE TABLE REGISTRATION (
ID BIGINT NOT NULL,
STUDENT_UUID VARCHAR(36) NOT NULL,
COURSE_UUID VARCHAR(36) NOT NULL,
PRIMARY KEY (ID),
FOREIGN KEY (STUDENT_UUID) REFERENCES STUDENT(UUID),
FOREIGN KEY (COURSE_UUID) REFERENCES COURSE(UUID)
);
ALTER TABLE REGISTRATION ADD CONSTRAINT STUDENT_AND_COURSE_UNIQUE UNIQUE (STUDENT_UUID, COURSE_UUID);