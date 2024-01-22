package com.prod.hydraulicsystemsmaintenance.entities;

public class Valve extends Component {
    private Double flowRate;
    private Double pressure;
    public Valve(String name, String serialNumber, Double flowRate, Double pressure) {
        super(name, serialNumber);
        this.flowRate = flowRate;
        this.pressure = pressure;
    }
}
