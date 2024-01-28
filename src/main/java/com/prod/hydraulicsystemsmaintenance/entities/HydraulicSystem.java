package com.prod.hydraulicsystemsmaintenance.entities;

public class HydraulicSystem {
    private Long id;
    private String name;
    private Actuator actuator;
    private Pump pump;
    private Reservoir reservoir;
    private Valve valve;

    public HydraulicSystem(Long id, String name, Actuator actuator, Pump pump, Reservoir reservoir, Valve valve) {
        this.name = name;
        this.id = id;
        this.actuator = actuator;
        this.pump = pump;
        this.reservoir = reservoir;
        this.valve = valve;
    }

    public HydraulicSystem(String name, Actuator actuator, Pump pump, Reservoir reservoir, Valve valve) {
        this.name = name;
        this.actuator = actuator;
        this.pump = pump;
        this.reservoir = reservoir;
        this.valve = valve;
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

    public Actuator getActuator() {
        return actuator;
    }

    public void setActuator(Actuator actuator) {
        this.actuator = actuator;
    }

    public Pump getPump() {
        return pump;
    }

    public void setPump(Pump pump) {
        this.pump = pump;
    }

    public Reservoir getReservoir() {
        return reservoir;
    }

    public void setReservoir(Reservoir reservoir) {
        this.reservoir = reservoir;
    }

    public Valve getValve() {
        return valve;
    }

    public void setValve(Valve valve) {
        this.valve = valve;
    }
}
