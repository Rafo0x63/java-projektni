package com.prod.hydraulicsystemsmaintenance.entities;

import java.time.LocalDate;
import java.util.UUID;

public abstract class Component {
    private Integer id;
    private String model;
    private String serialNumber;
    private LocalDate installationDate;

    public Component(Integer id, String model, String serialNumber, LocalDate installationDate) {
        this.id = id;
        this.model = model;
        this.serialNumber = serialNumber;
        this.installationDate = installationDate;
    }

    public Component(String model, String serialNumber, LocalDate installationDate) {
        this.model = model;
        this.serialNumber = serialNumber;
        this.installationDate = installationDate;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
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

    @Override
    public String toString() {
        return  "id=" + id + ",\n" +
                "model='" + model + "',\n" +
                "serialNumber='" + serialNumber + "',\n" +
                "installationDate=" + installationDate + "',";
    }
}
