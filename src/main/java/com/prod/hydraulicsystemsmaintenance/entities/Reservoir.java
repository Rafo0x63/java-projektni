package com.prod.hydraulicsystemsmaintenance.entities;

import java.time.LocalDate;

public class Reservoir extends Component {
    private Integer capacity;

    public Reservoir(Integer id, String model, String serialNumber, LocalDate installationDate, Integer capacity) {
        super(id, model, serialNumber, installationDate);
        this.capacity = capacity;
    }

    public Reservoir(String model, String serialNumber, Integer capacity, LocalDate installationDate) {
        super(model, serialNumber, installationDate);
        this.capacity = capacity;
    }

    public Integer getCapacity() {
        return capacity;
    }

    public void setCapacity(Integer capacity) {
        this.capacity = capacity;
    }

    @Override
    public String toString() {
        return "{" + super.toString() + ",\n" +
                "capacity=" + capacity +
                '}';
    }
}
