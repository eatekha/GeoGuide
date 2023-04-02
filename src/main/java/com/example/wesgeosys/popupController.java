package com.example.wesgeosys;

import javafx.fxml.FXML;
import javafx.stage.Stage;

public class popupController {
    @FXML private javafx.scene.control.Button closePopup;

    @FXML
    protected void ClosePopup(){
        // get a handle to the stage
        Stage stage = (Stage) closePopup.getScene().getWindow();
        // do what you have to do
        stage.close();
    }
}
