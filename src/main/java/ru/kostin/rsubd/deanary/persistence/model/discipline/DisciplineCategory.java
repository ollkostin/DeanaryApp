package ru.kostin.rsubd.deanary.persistence.model.discipline;

import javax.persistence.*;

@Entity
@Table(name = "discipline_category")
public class DisciplineCategory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String name;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
