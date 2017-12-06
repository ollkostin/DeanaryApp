package ru.kostin.rsubd.deanary.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.kostin.rsubd.deanary.persistence.model.Lecturer;

import java.util.List;

public interface LecturerRepository extends JpaRepository<Lecturer, Integer> {
    Lecturer findOneByName(String name);

    @Query(nativeQuery = true, value = "SELECT * FROM get_lecturer_with_max_student_exam_count()")
    List<Object[]> getLecturersWithMaxStudentCount();
}
