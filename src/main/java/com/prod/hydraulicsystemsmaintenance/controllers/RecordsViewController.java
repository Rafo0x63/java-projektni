package com.prod.hydraulicsystemsmaintenance.controllers;

import com.prod.hydraulicsystemsmaintenance.database.Database;
import com.prod.hydraulicsystemsmaintenance.entities.ServiceRecord;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Tab;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.util.ResourceBundle;

public class RecordsViewController implements Initializable {
    @FXML private TableColumn<ServiceRecord, String> modelTableColumn;
    @FXML private TableColumn<ServiceRecord, String> serialNumberTableColumn;
    @FXML private TableColumn<ServiceRecord, String> servicedByTableColumn;
    @FXML private TableColumn<ServiceRecord, String> servicedOnTableColumn;
    @FXML private TableView<ServiceRecord> tableView;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        modelTableColumn.setCellValueFactory(r -> new SimpleStringProperty(r.getValue().model()));
        serialNumberTableColumn.setCellValueFactory(r -> new SimpleStringProperty(r.getValue().serialNumber()));
        servicedByTableColumn.setCellValueFactory(r -> new SimpleStringProperty(r.getValue().user().toString()));
        servicedOnTableColumn.setCellValueFactory(r -> new SimpleStringProperty(r.getValue().servicedOn().toString()));

        tableView.setItems(FXCollections.observableArrayList(Database.getAllRecords()));
    }
}
