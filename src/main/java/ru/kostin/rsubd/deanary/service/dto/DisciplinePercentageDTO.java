package ru.kostin.rsubd.deanary.service.dto;

public class DisciplinePercentageDTO {
    private String name;
    private Double percent;

    public DisciplinePercentageDTO(Object[] o) {
        this.name = (String) o[1];
        this.percent = (Double) o[2];
    }

    public DisciplinePercentageDTO() {

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getPercent() {
        return percent;
    }

    public void setPercent(Double percent) {
        this.percent = percent;
    }
}
