package com.example.wesgeosys;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class addFloorController implements Initializable {
    @FXML
    private TextField floor;
    @FXML
    public Button submitButton;

    @FXML
    public Button cancelButton;

    private void submit(ActionEvent event) {
        System.out.println(floor.getText());
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

