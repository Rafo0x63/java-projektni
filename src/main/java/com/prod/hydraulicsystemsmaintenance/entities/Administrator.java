package com.prod.hydraulicsystemsmaintenance.entities;

public class Administrator extends User {
    private boolean isAdministratingASystem = false;
    public Administrator(Integer id, String name, String username, String password, Integer administrator) {
        super(id, name, username, password, administrator);
    }

    public Administrator(String name, String username, String password, Integer administrator) {
        super(name, username, password, administrator);
    }

    public Administrator(Integer id, String name, String username, Integer administrator) {
        super(id, name, username, administrator);
    }

    public Administrator(Integer id, String name, String username, Integer administrator, boolean isAdministratingASystem) {
        super(id, name, username, administrator);
        this.isAdministratingASystem = isAdministratingASystem;
    }

    public boolean isAdministratingASystem() {
        return isAdministratingASystem;
    }

    public void setAdministratingASystem(boolean bool) {
        this.isAdministratingASystem = bool;
    }
}
