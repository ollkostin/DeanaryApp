DROP TABLE IF EXISTS control;

CREATE TABLE control (
  student_group_id SERIAL,
  discipline_id    SERIAL,
  lecturer_id      SERIAL,
  control_type     VARCHAR(255) NOT NULL,
  "date"           TIMESTAMP    NOT NULL,
  semester         VARCHAR(6)   NULL,
  PRIMARY KEY (student_group_id, discipline_id, "date"),
  FOREIGN KEY (student_group_id) REFERENCES student_group (id) ON DELETE CASCADE ON UPDATE CASCADE,
  FOREIGN KEY (discipline_id) REFERENCES discipline (id) ON DELETE CASCADE ON UPDATE CASCADE,
  FOREIGN KEY (lecturer_id) REFERENCES lecturer (id) ON UPDATE CASCADE
);

INSERT INTO control VALUES
  (1, 1, 1, 'EXAM', '2018-01-01', 'WINTER'),
  (2, 2, 2, 'TEST', '2018-01-02', 'WINTER'),
  (3, 3, 3, 'TEST', '2018-01-03', 'WINTER'),
  (4, 4, 4, 'EXAM', '2018-01-04', 'WINTER'),
  (5, 5, 5, 'EXAM', '2018-01-01', 'WINTER');

ALTER SEQUENCE control_discipline_id_seq RESTART WITH 6;
ALTER SEQUENCE control_student_group_id_seq RESTART WITH 6;