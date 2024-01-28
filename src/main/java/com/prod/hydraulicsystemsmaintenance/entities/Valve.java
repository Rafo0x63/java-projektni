package com.prod.hydraulicsystemsmaintenance.entities;

import java.time.LocalDate;

public class Valve extends Component {
    private Long flowRate;
    private Long pressure;
    public Valve(String name, String serialNumber, Long flowRate, Long pressure, LocalDate installationDate) {
        super(name, serialNumber, installationDate);
        this.flowRate = flowRate;
        this.pressure = pressure;
    }
}
