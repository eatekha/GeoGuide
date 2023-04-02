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
    /**
     * Represents the name of a new building that the user enters in a text field.
     */
    @FXML
    private TextField building;
    /**
     * The button the user clicks to submit the name of the building entered in the corresponding text field.
     */
    @FXML
    public Button submitButton;
    /**
     * The button the user presses to cancel and discard any changes
     */
    @FXML
    public Button cancelButton;
    /**
     * A static variable that stores the name of a building for reference across methods and classes.
     * <p>
     * Note: It is recommended to use this variable with caution and avoid modifying it frequently.
     */
    public static String bname;

    /**
     * Handles the action of the user submitting the name of a new building
     * Adds the building with the given name to the system
     *
     * @param event The event triggered by the user pressing the submit button
     */
    private void submit(ActionEvent event) {
        bname = building.getText();
        Stage stage = (Stage) submitButton.getScene().getWindow();
        stage.close();
    }

    /**
     * Handles the action of the user pressing the cancel button
     * Returns to the admin view without making any changes to the building list
     *
     * @param event The event triggered by the user pressing the cancel button
     */
    private void cancel(ActionEvent event) {
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
    }

    /**
     * Initializes the controller with the specified URL and ResourceBundle.
     * Sets the submitButton and cancelButton with their respective handlers: submit() and cancel().
     *
     * @param url The location used to resolve relative paths for the root object, or null if the location is not known.
     * @param rb  The resource bundle used to localize the root object, or null if the root object was not localized.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        submitButton.setOnAction(this::submit);
        cancelButton.setOnAction(this::cancel);
    }
}

