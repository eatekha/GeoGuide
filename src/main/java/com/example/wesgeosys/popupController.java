package com.example.wesgeosys;

import javafx.fxml.FXML;
import javafx.stage.Stage;

public class popupController {
    @FXML private javafx.scene.control.Button closePopup;

    @FXML
    protected void ClosePopup(){
        Stage stage = (Stage) closePopup.getScene().getWindow();
        stage.close();
    }
}
