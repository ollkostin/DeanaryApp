package ru.kostin.rsubd.deanary.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.kostin.rsubd.deanary.persistence.model.discipline.Discipline;

import java.util.List;

public interface DisciplineRepository extends JpaRepository<Discipline, Integer> {
    Discipline findOneByName(String name);

    @Query(nativeQuery = true,
            value = "SELECT * FROM get_percent_of_disciplines(:groupId)")
    List<Object[]> getGroupDisciplinePercentage(@Param("groupId") Integer groupId);
}
