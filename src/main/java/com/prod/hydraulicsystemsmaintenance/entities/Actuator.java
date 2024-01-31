package com.prod.hydraulicsystemsmaintenance.entities;

import com.prod.hydraulicsystemsmaintenance.Application;
import com.prod.hydraulicsystemsmaintenance.database.Database;

import java.time.LocalDate;

public non-sealed class Actuator extends Component implements Serviceable {
    private Integer force;

    public Actuator(Integer id, String model, String serialNumber, LocalDate installationDate, Integer force) {
        super(id, model, serialNumber, installationDate);
        this.force = force;
    }

    public Actuator(String model, String serialNumber, Integer force, LocalDate installationDate) {
        super(model, serialNumber, installationDate);
        this.force = force;
    }

    public Actuator(Integer id, String model, String serialNumber, LocalDate installationDate, Integer force, boolean isInstalledInSystem) {
        super(id, model, serialNumber, installationDate, isInstalledInSystem);
        this.force = force;
    }

    public Integer getForce() {
        return force;
    }

    public void setForce(Integer force) {
        this.force = force;
    }

    @Override
    public String toString() {
        return "{" + super.toString() + "\n" +
                "force=" + force +
                '}';
    }

    @Override
    public void service() {
        Database.updateActuator(this.getId(), this);
        Database.insertRecord(new ServiceRecord(this.getModel(), this.getSerialNumber(), LocalDate.now(), Application.currentUser.getName()));
    }
}
