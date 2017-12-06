package ru.kostin.rsubd.deanary.service.dto;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import ru.kostin.rsubd.deanary.persistence.model.discipline.Discipline;

public class DisciplineDTO {
    private Integer id;
    private StringProperty name;
    private StringProperty category;
    private Integer hoursAmount;

    public DisciplineDTO(Discipline d) {
        this.id = d.getId();
        this.name = new SimpleStringProperty(d.getName());
        this.hoursAmount = d.getHoursAmount();
        this.category = new SimpleStringProperty(d.getCategory().getName());
    }

    public DisciplineDTO() {
        this.name = new SimpleStringProperty();
        this.category = new SimpleStringProperty();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name.get();
    }

    public StringProperty nameProperty() {
        return name;
    }

    public void setName(String name) {
        this.name.set(name);
    }

    public Integer getHoursAmount() {
        return hoursAmount;
    }

    public void setHoursAmount(Integer hoursAmount) {
        this.hoursAmount = hoursAmount;
    }

    public String getCategory() {
        return category.get();
    }

    public StringProperty categoryProperty() {
        return category;
    }

    public void setCategory(String category) {
        this.category.set(category);
    }
}
