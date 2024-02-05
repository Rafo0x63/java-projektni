package com.prod.hydraulicsystemsmaintenance.generics;

import com.prod.hydraulicsystemsmaintenance.database.Database;
import com.prod.hydraulicsystemsmaintenance.entities.Component;
import com.prod.hydraulicsystemsmaintenance.exceptions.SerialNumberConflictException;

import java.util.ArrayList;
import java.util.List;

public class ComponentCheck<T extends Component> {
    T component;

    public ComponentCheck(T component) {
        this.component = component;
    }

    public boolean check() throws SerialNumberConflictException {
        List<Component> components = new ArrayList<>(Database.getAllActuators());
        components.addAll(Database.getAllPumps());
        components.addAll(Database.getAllReservoirs());
        components.addAll(Database.getAllValves());

        components = components.stream().filter(c -> this.component.getSerialNumber().compareTo(c.getSerialNumber()) == 0).toList();
        if (components.isEmpty()) return false;
        else throw new SerialNumberConflictException("A component with this serial number already exists!");
    }
}
