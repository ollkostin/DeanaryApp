package ru.kostin.rsubd.deanary.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.kostin.rsubd.deanary.persistence.model.StudentGroup;
import ru.kostin.rsubd.deanary.persistence.repository.DisciplineRepository;
import ru.kostin.rsubd.deanary.persistence.repository.StudentGroupRepository;
import ru.kostin.rsubd.deanary.service.dto.DisciplinePercentageDTO;
import ru.kostin.rsubd.deanary.service.dto.StudentGroupDTO;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class StudentGroupService {
    private StudentGroupRepository studentGroupRepository;
    private DisciplineRepository disciplineRepository;

    @Transactional
    public List<StudentGroupDTO> fetch() {
        return studentGroupRepository
                .findAll()
                .stream()
                .map(StudentGroupDTO::new)
                .collect(Collectors.toList());
    }

    @Transactional
    public StudentGroup save(StudentGroupDTO dto) {
        StudentGroup group = dto.getId() == null ?
                new StudentGroup() : studentGroupRepository.findOne(dto.getId());
        group.setName(dto.getName());
        group.setCourse(dto.getCourse());
        group.setStudAmount(dto.getStudAmount());
        return studentGroupRepository.save(group);
    }

    @Transactional
    public void delete(Integer id) {
        studentGroupRepository.delete(studentGroupRepository.findOne(id));
    }

    @Transactional
    public List<DisciplinePercentageDTO> getGroupDisciplinePercentage(Integer groupId) {
        return disciplineRepository.getGroupDisciplinePercentage(groupId)
                .stream()
                .map(DisciplinePercentageDTO::new)
                .collect(Collectors.toList());
    }

    @Autowired
    public StudentGroupService(StudentGroupRepository studentGroupRepository, DisciplineRepository disciplineRepository) {
        this.studentGroupRepository = studentGroupRepository;
        this.disciplineRepository = disciplineRepository;
    }

}
