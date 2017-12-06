package ru.kostin.rsubd.deanary.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.kostin.rsubd.deanary.persistence.model.control.Control;
import ru.kostin.rsubd.deanary.persistence.model.control.ControlId;

public interface ControlRepository extends JpaRepository<Control, ControlId> {
}
