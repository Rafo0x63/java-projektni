package com.prod.hydraulicsystemsmaintenance.entities;

public class Administrator extends User {
    public Administrator(Integer id, String name, String username, String password, Integer administrator) {
        super(id, name, username, password, administrator);
    }

    public Administrator(String name, String username, String password, Integer administrator) {
        super(name, username, password, administrator);
    }

    public Administrator(Integer id, String name, String username, Integer administrator) {
        super(id, name, username, administrator);
    }

}
