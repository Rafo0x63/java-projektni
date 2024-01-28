package com.prod.hydraulicsystemsmaintenance.entities;

import java.time.LocalDate;

public class Actuator extends Component {
    private Long force;

    public Actuator(Long id, String name, String serialNumber, LocalDate installationDate, Long force) {
        super(id, name, serialNumber, installationDate);
        this.force = force;
    }

    public Actuator(String name, String serialNumber, Long force, LocalDate installationDate) {
        super(name, serialNumber, installationDate);
        this.force = force;
    }

    public Long getForce() {
        return force;
    }

    public void setForce(Long force) {
        this.force = force;
    }
}
