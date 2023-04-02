package com.example.wesgeosys;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;


public class admMapController implements Initializable {
    /**
     * Button used to submit the form or perform an action.
     */
    @FXML
    public Button submitButton;
    /**
     * Button used to cancel the form or cancel an action.
     */
    @FXML
    public Button cancelButton;

    /**
     * Handles the action of the user pressing the submit button.
     * Prints the entered floor number and closes the window.
     *
     * @param event The event object representing the button press
     */
    private void submit(ActionEvent event) {
        Stage stage = (Stage) submitButton.getScene().getWindow();
        stage.close();
    }

    /**
     * Handles the action of the user pressing the cancel button.
     * Closes the window without making any changes.
     *
     * @param event The event object representing the button press
     */
    private void cancel(ActionEvent event) {
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
    }

    /**
     * Initializes the controller.
     * Sets the action handlers for the submit and cancel buttons.
     *
     * @param url            The URL location of the FXML file
     * @param resourceBundle The resource bundle used by the FXML file
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
    }
}

