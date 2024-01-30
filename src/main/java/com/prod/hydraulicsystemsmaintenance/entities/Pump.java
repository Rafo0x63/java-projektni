package com.prod.hydraulicsystemsmaintenance.entities;

import java.time.LocalDate;

public class Pump extends Component {
    private Integer flowRate;
    private Integer pressure;

    public Pump(Integer id, String name, String serialNumber, LocalDate installationDate, Integer flowRate, Integer pressure) {
        super(id, name, serialNumber, installationDate);
        this.flowRate = flowRate;
        this.pressure = pressure;
    }

    public Pump(String name, String serialNumber, Integer flowRate, Integer pressure, LocalDate installationDate) {
        super(name, serialNumber, installationDate);
        this.flowRate = flowRate;
        this.pressure = pressure;
    }

    public Pump(Integer id, String model, String serialNumber, LocalDate installationDate, Integer flowRate, Integer pressure, boolean isInstalledInSystem) {
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
}
