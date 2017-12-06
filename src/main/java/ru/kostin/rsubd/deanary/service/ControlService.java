package ru.kostin.rsubd.deanary.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.kostin.rsubd.deanary.persistence.model.Lecturer;
import ru.kostin.rsubd.deanary.persistence.model.StudentGroup;
import ru.kostin.rsubd.deanary.persistence.model.control.Control;
import ru.kostin.rsubd.deanary.persistence.model.control.ControlId;
import ru.kostin.rsubd.deanary.persistence.model.control.ControlType;
import ru.kostin.rsubd.deanary.persistence.model.discipline.Discipline;
import ru.kostin.rsubd.deanary.persistence.repository.ControlRepository;
import ru.kostin.rsubd.deanary.persistence.repository.DisciplineRepository;
import ru.kostin.rsubd.deanary.persistence.repository.LecturerRepository;
import ru.kostin.rsubd.deanary.persistence.repository.StudentGroupRepository;
import ru.kostin.rsubd.deanary.service.dto.ControlDTO;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ControlService {
    @Autowired
    private ControlRepository controlRepository;
    @Autowired
    private LecturerRepository lecturerRepository;
    @Autowired
    private StudentGroupRepository studentGroupRepository;
    @Autowired
    private DisciplineRepository disciplineRepository;

    @Transactional
    public List<ControlDTO> fetch() {
        return controlRepository
                .findAll()
                .stream()
                .map(ControlDTO::new)
                .collect(Collectors.toList());
    }

    @Transactional
    public Control save(ControlDTO dto) {
        Control control = new Control();
        StudentGroup group = studentGroupRepository.findOneByName(dto.getGroup());
        Lecturer lecturer = lecturerRepository.findOneByName(dto.getLecturer());
        Discipline discipline = disciplineRepository.findOneByName(dto.getDiscipline());
        control.setStudentGroup(group);
        control.setLecturer(lecturer);
        control.setDiscipline(discipline);
        control.setDate(dto.getDate());
        control.setControlType(ControlType.valueOf(dto.getControlType()));
        controlRepository.save(control);
        return control;
    }


    @Transactional
    public Control edit(ControlDTO dto) {
        Control control = controlRepository.findOne(dto.getId());
        controlRepository.delete(control);
        control = new Control();
        StudentGroup group = studentGroupRepository.findOneByName(dto.getGroup());
        Lecturer lecturer = lecturerRepository.findOneByName(dto.getLecturer());
        Discipline discipline = disciplineRepository.findOneByName(dto.getDiscipline());
        control.setStudentGroup(group);
        control.setLecturer(lecturer);
        control.setDiscipline(discipline);
        control.setDate(dto.getDate());
        control.setControlType(ControlType.valueOf(dto.getControlType()));
        controlRepository.save(control);
        return control;
    }


    @Transactional
    public void delete(ControlId id) {
        controlRepository.delete(id);
    }
}
