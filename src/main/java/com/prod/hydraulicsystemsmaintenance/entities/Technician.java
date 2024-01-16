package com.prod.hydraulicsystemsmaintenance.entities;

public class Technician extends User {
    public Technician(int id, String name, String username, String password, int administrator) {
        super(id, name, username, password, administrator);
    }

    public Technician(String name, String username, String password, int administrator) {
        super(name, username, password, administrator);
    }

    public Technician(Integer id, String name, String username, Integer administrator) {
        super(id, name, username, administrator);
    }
}
