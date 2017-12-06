package ru.kostin.rsubd.deanary.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.kostin.rsubd.deanary.persistence.model.StudentGroup;

public interface StudentGroupRepository extends JpaRepository<StudentGroup, Integer> {
    StudentGroup findOneByName(String name);
}
