package com.example.wesgeosys;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class editPOIController implements Initializable {
    /**
     * Represents a text field for the name.
     */
    @FXML
    public TextField name;
    /**
     * Represents a text field for the description.
     */
    @FXML
    public TextField desc;
    /**
     * Represents a text field for the layer type.
     */
    @FXML
    public TextField layerType;
    /**
     * Represents a text field for the room number.
     */
    @FXML
    public TextField roomNum;
    /**
     * Represents a text field for the x coordinate.
     */
    @FXML
    public TextField x;
    /**
     * Represents a text field for the y coordinate.
     */
    @FXML
    public TextField y;
    /**
     * Represents a button for submitting the form data.
     */
    @FXML
    public Button submitButton;
    /**
     * Represents a button for cancelling the form.
     */
    @FXML
    public Button cancelButton;
    /**
     * The name entered in the name text field.
     */
    public static String pname;
    /**
     * The description entered in the description text field.
     */
    public static String pdesc;
    /**
     * The room number entered in the room number text field.
     */
    public static String proom;
    /**
     * The layer type entered in the layer type text field.
     */
    public static String player;

    /**
     * Called when the submit button is clicked.
     * Stores the entered data in the corresponding variables and closes the form.
     *
     * @param event the event that triggered this method
     */
    private void submit(ActionEvent event) {
        pname = name.getText();
        pdesc = desc.getText();
        proom = roomNum.getText();
        player = layerType.getText();
        Stage stage = (Stage) submitButton.getScene().getWindow();
        stage.close();
    }

    /**
     * Called when the cancel button is clicked.
     * Closes the form.
     *
     * @param event the event that triggered this method
     */
    private void cancel(ActionEvent event) {
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
    }

    /**
     * Initializes the form by setting up the submit and cancel button actions.
     *
     * @param url the location used to resolve relative paths for the root object, or null if the location is not known.
     * @param rb  the resources used to localize the root object, or null if the root object was not localized.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        submitButton.setOnAction(this::submit);
        cancelButton.setOnAction(this::cancel);
    }
}
