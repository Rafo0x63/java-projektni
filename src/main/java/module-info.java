module com.prod.hydraulicssystemsmaintenance {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires org.slf4j;


    opens com.prod.hydraulicsystemsmaintenance to javafx.fxml;
    exports com.prod.hydraulicsystemsmaintenance;
    exports com.prod.hydraulicsystemsmaintenance.controllers;
    opens com.prod.hydraulicsystemsmaintenance.controllers to javafx.fxml;
    opens com.prod.hydraulicsystemsmaintenance.entities to javafx.base;
    exports com.prod.hydraulicsystemsmaintenance.entities;
}