package com.prod.hydraulicsystemsmaintenance.entities;

import java.util.UUID;

public abstract class Component {
    private Integer id;
    private String name;
    private String serialNumber;

    public Component(Integer id, String name, String serialNumber) {
        this.id = id;
        this.name = name;
        this.serialNumber = serialNumber;
    }

    public Component(String name, String serialNumber) {
        this.name = name;
        this.serialNumber = serialNumber;
    }
}
