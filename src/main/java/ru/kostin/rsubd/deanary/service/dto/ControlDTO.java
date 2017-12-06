package ru.kostin.rsubd.deanary.service.dto;

import javafx.beans.property.SimpleStringProperty;
import ru.kostin.rsubd.deanary.persistence.model.control.Control;
import ru.kostin.rsubd.deanary.persistence.model.control.ControlId;

import java.util.Date;

public class ControlDTO {
    private ControlId id;
    private SimpleStringProperty group;
    private SimpleStringProperty discipline;
    private SimpleStringProperty lecturer;
    private SimpleStringProperty controlType;
    private Date date;

    public ControlDTO() {
        this.group = new SimpleStringProperty();
        this.discipline = new SimpleStringProperty();
        this.lecturer = new SimpleStringProperty();
        this.controlType = new SimpleStringProperty();
    }

    public ControlDTO(Control c) {
        this.id = c.getId();
        this.group = new SimpleStringProperty(c.getStudentGroup().getName());
        this.discipline = new SimpleStringProperty(c.getDiscipline().getName());
        this.lecturer = new SimpleStringProperty(c.getLecturer().getName());
        this.controlType = new SimpleStringProperty(c.getControlType().name());
        this.date = c.getDate();
    }

    public String getGroup() {
        return group.get();
    }

    public SimpleStringProperty groupProperty() {
        return group;
    }

    public void setGroup(String group) {
        this.group.set(group);
    }

    public String getDiscipline() {
        return discipline.get();
    }

    public SimpleStringProperty disciplineProperty() {
        return discipline;
    }

    public void setDiscipline(String discipline) {
        this.discipline.set(discipline);
    }

    public String getLecturer() {
        return lecturer.get();
    }

    public SimpleStringProperty lecturerProperty() {
        return lecturer;
    }

    public void setLecturer(String lecturer) {
        this.lecturer.set(lecturer);
    }

    public String getControlType() {
        return controlType.get();
    }

    public SimpleStringProperty controlTypeProperty() {
        return controlType;
    }

    public void setControlType(String controlType) {
        this.controlType.set(controlType);
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public ControlId getId() {
        return id;
    }

    public void setId(ControlId id) {
        this.id = id;
    }
}
