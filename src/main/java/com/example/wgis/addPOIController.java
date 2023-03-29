package com.example.wgis;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import java.net.URL;
import java.util.ResourceBundle;

public class addPOIController implements Initializable {
    @FXML
    public TextField name;
    @FXML
    public TextField desc;
    @FXML
    public Button button_login;
    @FXML
    public Button button_login1;
    public static String newpname;
    public static String newpdesc;
    private void submit(ActionEvent event) {
        newpname = name.getText();
        newpdesc = desc.getText();
        Stage stage = (Stage)button_login.getScene().getWindow();
        stage.close();
    }

    private void cancel(ActionEvent event){
        Stage stage = (Stage)button_login1.getScene().getWindow();
        stage.close();
    }
    @Override
    public void initialize(URL url, ResourceBundle rb){
        button_login.setOnAction(this::submit);
        button_login1.setOnAction(this::cancel);
    }
}
