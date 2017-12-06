package ru.kostin.rsubd.deanary.persistence.model;

import org.hibernate.annotations.Formula;

import javax.persistence.*;

@Entity
@Table(name = "student_group")
public class StudentGroup {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String name;
    private Integer course;
    private Integer studAmount;
    private Integer hoursAmount;
    @Formula("(select * from count_session_length(id,'WINTER'))")
    private Integer winterSessionLength;
    @Formula("(select * from count_session_length(id,'SUMMER'))")
    private Integer summerSessionLength;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
