-- передача контроля при удалении преподавателя
CREATE OR REPLACE FUNCTION on_delete_lecturer()
  RETURNS TRIGGER AS
$$
DECLARE
  r RECORD;
BEGIN
  SELECT *
  INTO r
  FROM lecturer
  WHERE id <> old.id
  ORDER BY id
  LIMIT 1;
  IF r IS NULL
  THEN
    TRUNCATE lecturer;
    RETURN NULL;
  ELSE
    UPDATE control
    SET lecturer_id = r.id
    WHERE lecturer_id = old.id;
    RETURN old;
  END IF;
END;
$$
LANGUAGE PLPGSQL;

CREATE TRIGGER on_delete_lecturer
  BEFORE DELETE
  ON lecturer
  FOR EACH ROW EXECUTE PROCEDURE on_delete_lecturer();

CREATE OR REPLACE FUNCTION count_group_hours_amount(group_id INTEGER)
  RETURNS INTEGER AS
$$
DECLARE
  result INTEGER;
BEGIN
  SELECT sum(d.hours_amount)
  INTO result
  FROM discipline d
    JOIN control c ON d.id = c.discipline_id
  WHERE d.id = discipline_id AND c.student_group_id = group_id;
  RETURN result;
END;
$$
LANGUAGE PLPGSQL;

--пересчет часов у группы при обновлении/удалении/вставке дисциплин
CREATE OR REPLACE FUNCTION count_group_hours_amount_trigger()
  RETURNS TRIGGER AS
$$
DECLARE
  disc_id INTEGER;
  r       RECORD;
BEGIN
  IF TG_OP = 'DELETE'
  THEN
    IF TG_TABLE_NAME = 'discipline'
    THEN
      disc_id := old.id;
    ELSEIF TG_TABLE_NAME = 'control'
      THEN
        disc_id := old.discipline_id;
    END IF;
  ELSEIF TG_OP = 'INSERT' OR TG_OP = 'UPDATE'
    THEN
      IF TG_TABLE_NAME = 'discipline'
      THEN
        disc_id := new.id;
      ELSEIF TG_TABLE_NAME = 'control'
        THEN
          disc_id := new.discipline_id;
      END IF;
  END IF;
  FOR r IN SELECT c.student_group_id AS group_id
           FROM control c
           WHERE c.discipline_id = disc_id
  LOOP
    UPDATE student_group
    SET hours_amount = count_group_hours_amount(r.group_id)
    WHERE id = r.group_id;
  END LOOP;
  IF TG_OP = 'DELETE'
  THEN
    RETURN old;
  ELSEIF TG_OP = 'INSERT' OR TG_OP = 'UPDATE'
    THEN
      RETURN new;
  END IF;
END;
$$
LANGUAGE PLPGSQL;

CREATE TRIGGER count_group_hours_amount_discipline_trigger
  AFTER UPDATE OR DELETE OR INSERT
  ON discipline
  FOR EACH ROW EXECUTE PROCEDURE count_group_hours_amount_trigger();

CREATE TRIGGER count_group_hours_amount_control_trigger
  AFTER DELETE OR UPDATE OF discipline_id OR INSERT
  ON control
  FOR EACH ROW EXECUTE PROCEDURE count_group_hours_amount_trigger();

CREATE OR REPLACE FUNCTION get_semester(t TIMESTAMP)
  RETURNS VARCHAR(6) AS
$$
BEGIN
  IF date_part('month', t) IN (9, 10, 11, 12, 1, 2)
  THEN
    RETURN 'WINTER';
  ELSEIF date_part('month', t) IN (3, 4, 5, 6, 7, 8)
    THEN
      RETURN 'SUMMER';
  END IF;
END;
$$
LANGUAGE PLPGSQL;

CREATE OR REPLACE FUNCTION set_semester()
  RETURNS TRIGGER AS
$$
BEGIN
  new.semester := get_semester(new.date);
  RETURN new;
END;
$$
LANGUAGE PLPGSQL;

CREATE TRIGGER set_semester
  BEFORE UPDATE OR INSERT
  ON control
  FOR EACH ROW EXECUTE PROCEDURE set_semester();