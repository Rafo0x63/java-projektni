package com.prod.hydraulicsystemsmaintenance.entities;

import java.time.LocalDate;

public class Reservoir extends Component {
    private Long capacity;

    public Reservoir(Integer id, String model, String serialNumber, LocalDate installationDate, Long capacity) {
        super(id, model, serialNumber, installationDate);
        this.capacity = capacity;
    }

    public Reservoir(String model, String serialNumber, Long capacity, LocalDate installationDate) {
        super(model, serialNumber, installationDate);
        this.capacity = capacity;
    }

    public Long getCapacity() {
        return capacity;
    }

    public void setCapacity(Long capacity) {
        this.capacity = capacity;
    }

    @Override
    public String toString() {
        return "{" + super.toString() + ",\n" +
                "capacity=" + capacity +
                '}';
    }
}
