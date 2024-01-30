package com.prod.hydraulicsystemsmaintenance.entities;

import com.prod.hydraulicsystemsmaintenance.Application;
import com.prod.hydraulicsystemsmaintenance.database.Database;

import java.time.LocalDate;

public non-sealed class Valve extends Component implements Serviceable {
    private Integer flowRate;
    private Integer pressure;

    public Valve(Integer id, String name, String serialNumber, LocalDate installationDate, Integer flowRate, Integer pressure) {
        super(id, name, serialNumber, installationDate);
        this.flowRate = flowRate;
        this.pressure = pressure;
    }

    public Valve(String name, String serialNumber, Integer flowRate, Integer pressure, LocalDate installationDate) {
        super(name, serialNumber, installationDate);
        this.flowRate = flowRate;
        this.pressure = pressure;
    }

    public Valve(Integer id, String model, String serialNumber, LocalDate installationDate, Integer flowRate, Integer pressure, boolean isInstalledInSystem) {
        super(id, model, serialNumber, installationDate, isInstalledInSystem);
        this.flowRate = flowRate;
        this.pressure = pressure;
    }

    public Integer getFlowRate() {
        return flowRate;
    }

    public void setFlowRate(Integer flowRate) {
        this.flowRate = flowRate;
    }

    public Integer getPressure() {
        return pressure;
    }

    public void setPressure(Integer pressure) {
        this.pressure = pressure;
    }

    @Override
    public String toString() {
        return "{" + super.toString() + "\n" +
                "flowRate=" + flowRate + ",\n" +
                "pressure=" + pressure +
                '}';
    }

    @Override
    public void service() {
        Database.insertRecord(new ServiceRecord(this.getModel(), this.getSerialNumber(), LocalDate.now(), Application.currentUser.getName()));
    }
}
