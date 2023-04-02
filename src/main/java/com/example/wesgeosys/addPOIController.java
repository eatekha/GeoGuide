package com.example.wesgeosys;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class addPOIController implements Initializable {
    /**
     * This is a TextField object that is used to represent a name input field in a JavaFX graphical user interface.
     */
    @FXML
    public TextField name;
    /**
     * This is a TextField object that is used to represent a description input field in a JavaFX graphical user interface.
     */
    @FXML
    public TextField desc;
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
     * String variable to store a new product name.
     */
    public static String newpname;
    /**
     * String variable to store a new product description.
     */
    public static String newpdesc;

    /**
     * Handles the action of the user pressing the submit button.
     * Prints the entered floor number and closes the window.
     *
     * @param event The event object representing the button press
     */
    private void submit(ActionEvent event) {
        newpname = name.getText();
        newpdesc = desc.getText();
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
     * @param url The URL location of the FXML file
     * @param rb  The resource bundle used by the FXML file
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        submitButton.setOnAction(this::submit);
        cancelButton.setOnAction(this::cancel);
    }
}
