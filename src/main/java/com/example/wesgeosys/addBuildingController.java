package com.example.wesgeosys;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class addBuildingController implements Initializable {

    @FXML
    private TextField building;

    @FXML
    public Button submitButton;

    @FXML
    public Button cancelButton;

    public static String bname;

    private void submit(ActionEvent event) {
        bname = building.getText();
        Stage stage = (Stage) submitButton.getScene().getWindow();
        stage.close();
    }

    private void cancel(ActionEvent event) {
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        submitButton.setOnAction(this::submit);
        cancelButton.setOnAction(this::cancel);
    }
}

