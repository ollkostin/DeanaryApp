package ru.kostin.rsubd.deanary.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.kostin.rsubd.deanary.persistence.model.Lecturer;
import ru.kostin.rsubd.deanary.persistence.repository.LecturerRepository;
import ru.kostin.rsubd.deanary.service.dto.LecturerDTO;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class LecturerService {
    private LecturerRepository lecturerRepository;

    @Transactional
    public List<LecturerDTO> fetch() {
        return lecturerRepository.findAll()
                .stream()
                .map(LecturerDTO::new)
                .collect(Collectors.toList());
    }

    @Transactional
    public Integer save(LecturerDTO lecturerDTO) {
        Lecturer lecturer = lecturerDTO.getId() == null ? new Lecturer() : lecturerRepository.findOne(lecturerDTO.getId());
        lecturer.setName(lecturerDTO.getFullName());
        return lecturerRepository.save(lecturer).getId();
    }

    @Transactional
    public void delete(Integer id) {
        lecturerRepository.delete(id);
    }

    @Transactional
    public List<LecturerDTO> getLecturersWithMaxStudentCount() {
        return lecturerRepository.getLecturersWithMaxStudentCount()
                .stream()
                .map(LecturerDTO::new)
                .collect(Collectors.toList());
    }

    @Autowired
    public LecturerService(LecturerRepository lecturerRepository) {
        this.lecturerRepository = lecturerRepository;
    }
}
