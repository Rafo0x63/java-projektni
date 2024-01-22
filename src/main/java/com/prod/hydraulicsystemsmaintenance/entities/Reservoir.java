package com.prod.hydraulicsystemsmaintenance.entities;

public class Reservoir extends Component {
    private Double capacity;

    public Reservoir(String name, String serialNumber, Double capacity) {
        super(name, serialNumber);
        this.capacity = capacity;
    }
}
