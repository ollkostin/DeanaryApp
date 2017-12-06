package ru.kostin.rsubd.deanary.persistence.model.control;

import ru.kostin.rsubd.deanary.persistence.model.Lecturer;
import ru.kostin.rsubd.deanary.persistence.model.StudentGroup;
import ru.kostin.rsubd.deanary.persistence.model.discipline.Discipline;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "control")
public class Control {
    @EmbeddedId
    private ControlId id;
    @ManyToOne
    @MapsId("studentGroupId")
    private StudentGroup studentGroup;
    @ManyToOne
    @MapsId("disciplineId")
    private Discipline discipline;
    @ManyToOne
    private Lecturer lecturer;
    @Enumerated(EnumType.STRING)
    private ControlType controlType;

    public Control() {
        this.id = new ControlId();
    }

    public ControlId getId() {
        return id;
    }

    public void setId(ControlId id) {
        this.id = id;
    }

    public StudentGroup getStudentGroup() {
        return studentGroup;
    }

    public void setStudentGroup(StudentGroup studentGroup) {
        this.studentGroup = studentGroup;
    }

    public Discipline getDiscipline() {
        return discipline;
    }

    public void setDiscipline(Discipline discipline) {
        this.discipline = discipline;
    }

    public Lecturer getLecturer() {
        return lecturer;
    }

    public void setLecturer(Lecturer lecturer) {
        this.lecturer = lecturer;
    }

    public ControlType getControlType() {
        return controlType;
    }

    public void setControlType(ControlType controlType) {
        this.controlType = controlType;
    }

    public Date getDate() {
        return id.getDate();
    }

    public void setDate(Date date) {
        this.id.setDate(date);
    }
}
