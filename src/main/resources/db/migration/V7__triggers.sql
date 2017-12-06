--контроль экзаменов по семестрам (триггер перед вставкой)
CREATE OR REPLACE FUNCTION check_control_is_one_in_semester()
  RETURNS TRIGGER AS
$$
DECLARE
  r RECORD;
BEGIN
  SELECT c.*
  INTO r
  FROM control c
  WHERE c.student_group_id = new.student_group_id
        AND c.discipline_id = new.discipline_id
        AND c.semester = get_semester(new.date);
  IF r IS NOT NULL
  THEN
    RAISE EXCEPTION 'В этом семестре экзамен по дисциплине уже существует';
    RETURN NULL;
  ELSE
    RETURN new;
  END IF;
END;
$$
LANGUAGE PLPGSQL;

CREATE TRIGGER check_control_is_one_in_semester
  BEFORE INSERT OR UPDATE OF date
  ON control
  FOR EACH ROW EXECUTE PROCEDURE check_control_is_one_in_semester();

--ограничение на длительность сессии (не дольше месяца)
CREATE OR REPLACE FUNCTION check_session_length()
  RETURNS TRIGGER AS
$$
DECLARE
  total        INTEGER := 0;
  max_min_date RECORD;
  diff         INTEGER := 0;
  d1           INTEGER;
  d2           INTEGER;
  max_days     INTEGER := 30;
BEGIN
  SELECT
    max(c.date) AS max,
    min(c.date) AS min
  INTO max_min_date
  FROM control c
  WHERE c.student_group_id = new.student_group_id AND c.semester = get_semester(new.date);
  IF max_min_date IS NOT NULL
  THEN
    SELECT *
    INTO total
    FROM count_session_length(new.student_group_id, get_semester(new.date));
    d1 := abs(date_part('day', max_min_date.max) - date_part('day', new.date));
    d2 := abs(date_part('day', max_min_date.min) - date_part('day', new.date));
    IF d1 < d2
    THEN
      diff := d1;
    ELSE
      diff := d2;
    END IF;
  END IF;
  IF total + diff < max_days OR (new.date < max_min_date.max AND new.date > max_min_date.min)
  THEN
    RETURN new;
  ELSE
    RAISE EXCEPTION 'Сессия не может быть длиннее % дней', max_days;
    RETURN NULL;
  END IF;
END;
$$
LANGUAGE PLPGSQL;

CREATE TRIGGER check_session_length
  BEFORE INSERT OR UPDATE OF date
  ON control
  FOR EACH ROW EXECUTE PROCEDURE check_session_length();
