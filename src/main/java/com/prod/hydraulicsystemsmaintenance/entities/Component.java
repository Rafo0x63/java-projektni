package com.prod.hydraulicsystemsmaintenance.entities;

import java.time.LocalDate;
import java.util.UUID;

public abstract class Component {
    private Long id;
    private String name;
    private String serialNumber;
    private LocalDate installationDate;

    public Component(Long id, String name, String serialNumber, LocalDate installationDate) {
        this.id = id;
        this.name = name;
        this.serialNumber = serialNumber;
        this.installationDate = installationDate;
    }

    public Component(String name, String serialNumber, LocalDate installationDate) {
        this.name = name;
        this.serialNumber = serialNumber;
        this.installationDate = installationDate;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public LocalDate getInstallationDate() {
        return installationDate;
    }

    public void setInstallationDate(LocalDate installationDate) {
        this.installationDate = installationDate;
    }
}
