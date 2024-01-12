module com.prod.hydraulicssystemsmaintenance {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens com.prod.hydraulicssystemsmaintenance to javafx.fxml;
    exports com.prod.hydraulicssystemsmaintenance;
    exports com.prod.hydraulicssystemsmaintenance.controllers;
    opens com.prod.hydraulicssystemsmaintenance.controllers to javafx.fxml;
}