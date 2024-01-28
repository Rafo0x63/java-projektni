package com.prod.hydraulicsystemsmaintenance.entities;

public class Technician extends User {
    public Technician(Long id, String name, String username, String password, Integer administrator) {
        super(id, name, username, password, administrator);
    }

    public Technician(String name, String username, String password, Integer administrator) {
        super(name, username, password, administrator);
    }

    public Technician(Long id, String name, String username, Integer administrator) {
        super(id, name, username, administrator);
    }
}
