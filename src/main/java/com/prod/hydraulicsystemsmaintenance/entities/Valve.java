package com.prod.hydraulicsystemsmaintenance.entities;

import java.time.LocalDate;

public class Valve extends Component {
    private Long flowRate;
    private Long pressure;

    public Valve(Long id, String name, String serialNumber, LocalDate installationDate, Long flowRate, Long pressure) {
        super(id, name, serialNumber, installationDate);
        this.flowRate = flowRate;
        this.pressure = pressure;
    }

    public Valve(String name, String serialNumber, Long flowRate, Long pressure, LocalDate installationDate) {
        super(name, serialNumber, installationDate);
        this.flowRate = flowRate;
        this.pressure = pressure;
    }

    public Long getFlowRate() {
        return flowRate;
    }

    public void setFlowRate(Long flowRate) {
        this.flowRate = flowRate;
    }

    public Long getPressure() {
        return pressure;
    }

    public void setPressure(Long pressure) {
        this.pressure = pressure;
    }

    @Override
    public String toString() {
        return "{" + super.toString() +
                "flowRate=" + flowRate +
                ", pressure=" + pressure +
                '}';
    }
}
