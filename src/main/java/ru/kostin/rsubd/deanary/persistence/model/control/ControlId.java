package ru.kostin.rsubd.deanary.persistence.model.control;

import javax.persistence.Embeddable;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.io.Serializable;
import java.util.Date;

@Embeddable
public class ControlId implements Serializable {
    private Integer studentGroupId;
    private Integer disciplineId;
    @Temporal(TemporalType.TIMESTAMP)
    private Date date;

    public ControlId() {
    }

    public ControlId(Integer studentGroupId, Integer disciplineId, Date date) {
        this.studentGroupId = studentGroupId;
        this.disciplineId = disciplineId;
        this.date = date;
    }


    public Integer getDisciplineId() {
        return disciplineId;
    }

    public void setDisciplineId(Integer disciplineId) {
        this.disciplineId = disciplineId;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Integer getStudentGroupId() {
        return studentGroupId;
    }

    public void setStudentGroupId(Integer studentGroupId) {
        this.studentGroupId = studentGroupId;
    }
}
