package com.example.wgis;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import java.net.URL;
import java.util.ResourceBundle;

public class editFloorController implements Initializable {
    @FXML
    public TextField name;
    @FXML
    public Button submitButton;
    @FXML
    public Button loginButton;

    private void submit(ActionEvent event) {
        System.out.println(name.getText());
        Stage stage = (Stage)submitButton.getScene().getWindow();
        stage.close();
    }

    private void cancel(ActionEvent event){
        Stage stage = (Stage)loginButton.getScene().getWindow();
        stage.close();
    }

    @Override
    public void initialize(URL url, ResourceBundle rb){
        submitButton.setOnAction(this::submit);
        loginButton.setOnAction(this::cancel);
    }



}
