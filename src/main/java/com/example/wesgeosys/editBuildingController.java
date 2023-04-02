package com.example.wesgeosys;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class editBuildingController implements Initializable {
    /**
     * Represents a text field for entering a name to be edited.
     */
    @FXML
    public TextField name;
    /**
     * Represents a button for submitting the edited name.
     */
    @FXML
    public Button submitButton;
    /**
     * Represents a button for canceling the edit name operation.
     */
    @FXML
    public Button cancelButton;
    /**
     * Stores the name of the building being edited.
     */
    public static String editbname;

    /**
     * Event handler for submitting the edited name. Updates the editbname variable and closes the window.
     *
     * @param event The ActionEvent triggered by clicking the submitButton.
     */
    private void submit(ActionEvent event) {
        editbname = name.getText();
        Stage stage = (Stage) submitButton.getScene().getWindow();
        stage.close();
    }

    /**
     * Event handler for canceling the edit name operation. Closes the window.
     *
     * @param event The ActionEvent triggered by clicking the cancelButton.
     */
    private void cancel(ActionEvent event) {
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
    }

    /**
     * Initializes the submitButton and cancelButton with their respective event handlers.
     *
     * @param url The location of the FXML file.
     * @param rb  The resources used to localize the FXML file.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        submitButton.setOnAction(this::submit);
        cancelButton.setOnAction(this::cancel);
    }


}

