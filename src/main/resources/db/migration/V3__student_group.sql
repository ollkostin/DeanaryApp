DROP TABLE IF EXISTS student_group;

CREATE TABLE student_group (
  id           SERIAL,
  name         VARCHAR(10)       NOT NULL,
  course       INTEGER DEFAULT 1 NOT NULL CHECK (course > 0 AND course < 7),
  stud_amount  INTEGER DEFAULT 0 NOT NULL CHECK (stud_amount > -1 AND stud_amount < 35),
  hours_amount INTEGER DEFAULT 0 CHECK (hours_amount > -1),
  PRIMARY KEY (id),
  CONSTRAINT uc_student_group UNIQUE (name, course)
);

INSERT INTO student_group VALUES
  (1, 'A1', 1, 17, 90),
  (2, 'A2', 2, 23, 80),
  (3, 'A3', 3, 9, 60),
  (4, 'A4', 4, 23, 100),
  (5, 'A5', 3, 17, 40),
  (6, 'A6', 2, 20, 0);

ALTER SEQUENCE student_group_id_seq RESTART WITH 7;