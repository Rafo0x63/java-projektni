package com.prod.hydraulicsystemsmaintenance.entities;

public class Pump extends Component {
    private Double flowRate;
    private Double pressure;

    public Pump(String name, String serialNumber, Double flowRate, Double pressure) {
        super(name, serialNumber);
        this.flowRate = flowRate;
        this.pressure = pressure;
    }
}
