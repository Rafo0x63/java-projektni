package com.prod.hydraulicsystemsmaintenance.entities;

import java.time.LocalDate;

public record ServiceRecord(String model, String serialNumber, LocalDate servicedOn, String name) {
}
