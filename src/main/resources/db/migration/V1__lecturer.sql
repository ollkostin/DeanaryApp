DROP TABLE IF EXISTS lecturer;

CREATE TABLE lecturer (
  id   SERIAL,
  name VARCHAR(255) NOT NULL,
  PRIMARY KEY (id)
);

INSERT INTO lecturer VALUES
  (1, 'Иванов И.И.'),
  (2, 'Петров П.П.'),
  (3, 'Сидоров С.С.'),
  (4, 'Андреев А.А.'),
  (5, 'Костылев К.К.'),
  (6, 'Паль А.П.');

ALTER SEQUENCE lecturer_id_seq RESTART WITH 7;