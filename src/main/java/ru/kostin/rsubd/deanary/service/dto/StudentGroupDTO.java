package ru.kostin.rsubd.deanary.service.dto;

import javafx.beans.property.SimpleStringProperty;
import ru.kostin.rsubd.deanary.persistence.model.StudentGroup;

public class StudentGroupDTO {
    private Integer id;
    private SimpleStringProperty name;
    private Integer course;
    private Integer studAmount;
    private Integer hoursAmount;
    private Integer winterSessionLength;
    private Integer summerSessionLength;

    public StudentGroupDTO(StudentGroup studentGroup) {
        this.id = studentGroup.getId();
        this.name = new SimpleStringProperty(studentGroup.getName());
        this.course = studentGroup.getCourse();
        this.studAmount = studentGroup.getStudAmount();
        this.hoursAmount = studentGroup.getHoursAmount();
        this.winterSessionLength = studentGroup.getWinterSessionLength();
        this.summerSessionLength = studentGroup.getSummerSessionLength();
    }

    public StudentGroupDTO() {
        this.name = new SimpleStringProperty();
    }

    public String getName() {
        return name.get();
    }

    public SimpleStringProperty nameProperty() {
        return name;
    }

    public void setName(String name) {
        this.name.set(name);
    }

    public Integer getCourse() {
        return course;
    }

    public void setCourse(Integer course) {
        this.course = course;
    }

    public Integer getStudAmount() {
        return studAmount;
    }

    public void setStudAmount(Integer studAmount) {
        this.studAmount = studAmount;
    }

    public Integer getHoursAmount() {
        return hoursAmount;
    }

    public void setHoursAmount(Integer hoursAmount) {
        this.hoursAmount = hoursAmount;
    }

    public Integer getWinterSessionLength() {
        return winterSessionLength;
    }

    public void setWinterSessionLength(Integer winterSessionLength) {
        this.winterSessionLength = winterSessionLength;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getSummerSessionLength() {
        return summerSessionLength;
    }

    public void setSummerSessionLength(Integer summerSessionLength) {
        this.summerSessionLength = summerSessionLength;
    }
}
