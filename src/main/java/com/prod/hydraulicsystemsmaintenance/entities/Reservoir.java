package com.prod.hydraulicsystemsmaintenance.entities;

import java.time.LocalDate;

public class Reservoir extends Component {
    private Long capacity;

    public Reservoir(String name, String serialNumber, Long capacity, LocalDate installationDate) {
        super(name, serialNumber, installationDate);
        this.capacity = capacity;
    }
}
