DROP TABLE IF EXISTS discipline;
DROP TABLE IF EXISTS discipline_category;

CREATE TABLE discipline_category (
  id   SERIAL,
  name VARCHAR(255) NOT NULL,
  PRIMARY KEY (id)
);

INSERT INTO discipline_category VALUES
  (1, 'гуманитарная'),
  (2, 'математическая'),
  (3, 'компьютерная'),
  (4, 'общеинженерная');

CREATE TABLE discipline (
  id           SERIAL,
  name         VARCHAR(255)        NOT NULL,
  category_id  SERIAL              NOT NULL,
  hours_amount INTEGER DEFAULT 0   NOT NULL CHECK (hours_amount > 0 AND hours_amount < 200),
  PRIMARY KEY (id),
  CONSTRAINT uc_discipline_category UNIQUE (name, category_id),
  FOREIGN KEY (category_id) REFERENCES discipline_category (id)
);

ALTER SEQUENCE discipline_category_id_seq RESTART WITH 5;

INSERT INTO discipline VALUES
  (1, 'Физика', 2, 90),
  (2, 'Английский язык', 1, 80),
  (3, 'РПБД', 3, 60),
  (4, 'Теория игр', 2, 100),
  (5, 'Прикладное программное обеспечение', 4, 40);

ALTER SEQUENCE discipline_id_seq RESTART WITH 6;


