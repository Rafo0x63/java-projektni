package com.prod.hydraulicsystemsmaintenance.entities;

public sealed interface Serviceable permits Pump, Valve, Actuator {
    void service();
}
