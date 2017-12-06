package ru.kostin.rsubd.deanary.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.kostin.rsubd.deanary.persistence.model.discipline.Discipline;
import ru.kostin.rsubd.deanary.persistence.model.discipline.DisciplineCategory;
import ru.kostin.rsubd.deanary.persistence.repository.DisciplineCategoryRepository;
import ru.kostin.rsubd.deanary.persistence.repository.DisciplineRepository;
import ru.kostin.rsubd.deanary.service.dto.DisciplineDTO;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class DisciplineService {
    private DisciplineRepository disciplineRepository;
    private DisciplineCategoryRepository categoryRepository;

    @Transactional
    public List<DisciplineDTO> fetch() {
        return disciplineRepository.findAll()
                .stream()
                .map(DisciplineDTO::new)
                .collect(Collectors.toList());
    }

    @Transactional
    public List<String> getCategories() {
        return categoryRepository.findAll()
                .stream()
                .map(DisciplineCategory::getName)
                .collect(Collectors.toList());
    }

    @Transactional
    public void delete(Integer id) {
        disciplineRepository.delete(id);
    }

    @Transactional
    public Discipline save(DisciplineDTO dto) {
        Discipline d = dto.getId() != null ? disciplineRepository.findOne(dto.getId()) : new Discipline();
        d.setName(dto.getName());
        d.setHoursAmount(dto.getHoursAmount());
        d.setCategory(categoryRepository.findOneByName(dto.getCategory()));
        return disciplineRepository.save(d);
    }

    @Autowired
    public DisciplineService(DisciplineRepository disciplineRepository, DisciplineCategoryRepository categoryRepository) {
        this.disciplineRepository = disciplineRepository;
        this.categoryRepository = categoryRepository;
    }

}
