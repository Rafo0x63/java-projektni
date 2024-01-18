package com.prod.hydraulicsystemsmaintenance.entities;

public abstract class Component {
    private Integer id;
    private String name;

    public Component(Integer id, String name) {
        this.id = id;
        this.name = name;
    }

}
