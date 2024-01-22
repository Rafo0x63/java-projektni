package com.prod.hydraulicsystemsmaintenance.entities;

public class Acutator extends Component {
    private Double loadCapacity;
    private Double speed;
    public Acutator(String name, String serialNumber, Double loadCapacity, Double speed) {
        super(name, serialNumber);
        this.loadCapacity = loadCapacity;
        this.speed = speed;
    }
}
