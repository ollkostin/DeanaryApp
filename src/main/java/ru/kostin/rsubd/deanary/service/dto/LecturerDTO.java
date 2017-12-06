package ru.kostin.rsubd.deanary.service.dto;

import javafx.beans.property.SimpleStringProperty;
import ru.kostin.rsubd.deanary.persistence.model.Lecturer;

public class LecturerDTO {
    private Integer id;
    private SimpleStringProperty fullName;

    public LecturerDTO(Lecturer lecturer) {
        this.id = lecturer.getId();
        this.fullName = new SimpleStringProperty(lecturer.getName());
    }

    public LecturerDTO(Object[] o) {
        this.id = (Integer) o[0];
        this.fullName = new SimpleStringProperty((String) o[1]);
    }

    public LecturerDTO() {
        this.fullName = new SimpleStringProperty();
    }

    public String getFullName() {
        return fullName.get();
    }

    public SimpleStringProperty fullNameProperty() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName.set(fullName);
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}
