--Для каждой группы определить продолжительность сессии
CREATE OR REPLACE FUNCTION count_session_length(group_id INTEGER, _semester VARCHAR(6))
  RETURNS INTEGER AS
$$
DECLARE
  result INTEGER;
BEGIN
  SELECT CASE
         WHEN max(c.date) = min(c.date)
           THEN 1
         WHEN max(c.date) IS NULL AND min(c.date) IS NULL
           THEN 0
         ELSE date_part('day', (max(c.date) - min(c.date)))
         END
  INTO result
  FROM control c
  WHERE c.student_group_id = group_id
        AND c.semester = _semester;
  RETURN result;
END;
$$
LANGUAGE PLPGSQL;

--Определить преподавателя, который в сессию принимает экзамены (не зачеты) у наибольшего числа студентов
CREATE OR REPLACE FUNCTION get_lecturer_with_max_student_exam_count()
  RETURNS TABLE(id INTEGER, name VARCHAR(255)) AS
$$
BEGIN
  RETURN QUERY
  WITH count_exam AS (
      SELECT
        l.*,
        SUM(DISTINCT sg.stud_amount) AS sum_stud_amount
      FROM lecturer l
        JOIN control c
          ON c.lecturer_id = l.id
        JOIN student_group sg
          ON sg.id = c.student_group_id
      WHERE c.control_type = 'EXAM'
      GROUP BY l.id)

  SELECT
    ce.id,
    ce.name
  FROM
    count_exam ce
  WHERE ce.sum_stud_amount = (
    SELECT MAX(ce1.sum_stud_amount)
    FROM count_exam ce1
  )
  GROUP BY ce.id, ce.name;
  RETURN;
END;
$$
LANGUAGE PLPGSQL;

--Определить, какой процент от общего объема дисциплин, изучаемых группой, составляют дисциплины каждой категории
CREATE OR REPLACE FUNCTION get_percent_of_disciplines(sg_id INTEGER)
  RETURNS TABLE(
    id      INTEGER,
    name    VARCHAR(255),
    percent FLOAT
  ) AS
$$
DECLARE
  sum FLOAT;
BEGIN
  SELECT SUM(DISTINCT d.hours_amount)
  INTO sum
  FROM discipline d
    JOIN control c
      ON c.discipline_id = d.id
  WHERE c.student_group_id = sg_id;
  RETURN QUERY
  SELECT DISTINCT
    d.id,
    d.name,
    CAST(round(CAST(d.hours_amount AS FLOAT)) * 100 / sum AS FLOAT)
  FROM discipline d
    JOIN control c ON d.id = c.discipline_id
  WHERE c.student_group_id = sg_id;
  RETURN;
END;
$$
LANGUAGE PLPGSQL;

--Определить, не сдает ли какая-либо группа два экзамена в один день
CREATE OR REPLACE FUNCTION get_two_exams_at_one_day(sg_id INTEGER)
  RETURNS TABLE(
    student_group_id INTEGER,
    discipline_id    INTEGER,
    lecturer_id      INTEGER,
    date             TIMESTAMP
  ) AS
$$
BEGIN
  RETURN QUERY
  SELECT
    c.student_group_id,
    c.discipline_id,
    c.lecturer_id,
    c.date
  FROM control c
  WHERE c.student_group_id = sg_id
        AND c.date IN
            (SELECT c1.date
             FROM control c1
             WHERE c1.student_group_id = sg_id
             GROUP BY c1.date
             HAVING count(c1.date) > 1);
  RETURN;
END;
$$
LANGUAGE PLPGSQL;