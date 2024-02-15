package com.prod.hydraulicsystemsmaintenance.entities;

import java.util.Set;

public class Technician extends User {
    private Set<Component> responsibleFor;
    public Technician(Integer id, String name, String username, String password, Integer administrator) {
        super(id, name, username, password, administrator);
    }

    public Technician(String name, String username, String password, Integer administrator) {
        super(name, username, password, administrator);
    }

    public Technician(Integer id, String name, String username, Integer administrator) {
        super(id, name, username, administrator);
    }

    public Technician(Integer id, String name, String username, Integer administrator, Set<Component> responsibleFor) {
        super(id, name, username, administrator);
        this.responsibleFor = responsibleFor;
    }
    public Set<Component> getResponsibleFor() {
        return responsibleFor;
    }

    public void setResponsibleFor(Set<Component> responsibleFor) {
        this.responsibleFor = responsibleFor;
    }

    public String equipmentString() {
        StringBuilder sb = new StringBuilder("{");
        for (Component c : responsibleFor) {
            sb.append(STR."\{c.toChangeString()},\n");
        }
        return sb.substring(0, sb.toString().length()-2).concat("}");
    }
}
