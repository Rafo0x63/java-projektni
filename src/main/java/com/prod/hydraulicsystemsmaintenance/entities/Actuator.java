package com.prod.hydraulicsystemsmaintenance.entities;

import java.time.LocalDate;

public class Actuator extends Component {
    private Long force;

    public Actuator(Long id, String model, String serialNumber, LocalDate installationDate, Long force) {
        super(id, model, serialNumber, installationDate);
        this.force = force;
    }

    public Actuator(String model, String serialNumber, Long force, LocalDate installationDate) {
        super(model, serialNumber, installationDate);
        this.force = force;
    }

    public Long getForce() {
        return force;
    }

    public void setForce(Long force) {
        this.force = force;
    }
}
