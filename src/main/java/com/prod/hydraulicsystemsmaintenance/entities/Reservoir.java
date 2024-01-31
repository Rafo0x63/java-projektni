package com.prod.hydraulicsystemsmaintenance.entities;

import com.prod.hydraulicsystemsmaintenance.database.Database;

import java.time.LocalDate;

public class Reservoir extends Component implements Replacable {
    private Integer capacity;

    public Reservoir(Integer id, String model, String serialNumber, LocalDate installationDate, Integer capacity) {
        super(id, model, serialNumber, installationDate);
        this.capacity = capacity;
    }

    public Reservoir(String model, String serialNumber, Integer capacity, LocalDate installationDate) {
        super(model, serialNumber, installationDate);
        this.capacity = capacity;
    }

    public Reservoir(Integer id, String model, String serialNumber, LocalDate installationDate, Integer capacity, boolean isInstalledInSystem) {
        super(id, model, serialNumber, installationDate, isInstalledInSystem);
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
        return "{" + super.toString() + "\n" +
                "capacity=" + capacity +
                '}';
    }

    @Override
    public void replace(Component component) {
        Database.updateReservoir(component.getId(), this);
    }
}
