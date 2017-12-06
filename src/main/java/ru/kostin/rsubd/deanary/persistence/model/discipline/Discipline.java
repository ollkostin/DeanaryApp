package ru.kostin.rsubd.deanary.persistence.model.discipline;

import javax.persistence.*;

@Entity
@Table(name = "discipline")
public class Discipline {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String name;
    @ManyToOne
    @JoinColumn(name = "category_id")
    private DisciplineCategory category;
    private Integer hoursAmount;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public DisciplineCategory getCategory() {
        return category;
    }

    public void setCategory(DisciplineCategory category) {
        this.category = category;
    }

    public Integer getHoursAmount() {
        return hoursAmount;
    }

    public void setHoursAmount(Integer hoursAmount) {
        this.hoursAmount = hoursAmount;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}
