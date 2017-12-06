package ru.kostin.rsubd.deanary.persistence.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import ru.kostin.rsubd.deanary.persistence.model.discipline.DisciplineCategory;

public interface DisciplineCategoryRepository extends JpaRepository<DisciplineCategory, Integer> {
    DisciplineCategory findOneByName(String name);
}
