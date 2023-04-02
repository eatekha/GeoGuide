package com.example.wesgeosys;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.json.simple.parser.ParseException;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;


public class signupController implements Initializable {
    /**
     * The password entered by the user in the textfield
     */
    @FXML
    private TextField tf_password;

    /**
     * The username entered by the user in the textfield
     */
    @FXML
    private TextField tf_username;

    /**
     * The button pressed by the user to go back to the login screen
     */
    @FXML
    public Button button_log_in;

    /**
     * The button pressed by the user to submit their new account details
     */
    @FXML
    Button button_signup;

    /**
     * The database used to check if the username is already taken
     */
    accountClass num1 = new accountClass("src/main/java/com/example/wesgeosys/accountData.json");

    /**
     * The boolean value for if the username is already taken
     */
    boolean validUser = false;

    /**
     * Handles the action of the sign up button being pressed
     * If username is already taken, taken to invalid signup screen
     * If username is valid, taken to MainMaps, account details saved to database
     * @param event
     */
    private void submit(ActionEvent event) {
        try{
            // Checks if username is already taken
            if(num1.checkValidUsername(tf_username.getText())){
                validUser = true;
            }
            // If the username is valid (not taken)
            if(validUser){
                try {
                    // Creates the account and adds it to the database
                    num1.createAccount(tf_username.getText(), tf_password.getText());
                    mainMapsController.username = tf_username.getText();
                    mainMapsController.adminPermissions = false;
                    // Opens the MainMaps
                    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("mainMapsGUI.fxml"));
                    Scene scene = new Scene(fxmlLoader.load(), 322.0, 391.0);
                    Stage stage = new Stage();
                    stage.setTitle("Main Maps System");
                    stage.setMaximized(true);
                    stage.setScene(scene);
                    stage.show();
                    ((Node)(event.getSource())).getScene().getWindow().hide();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }else{ // If the username is invalid (taken)
                try {
                    // Switches to Invalid Login Screen
                    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("signupInvalidGUI.fxml"));
                    Scene scene = new Scene(fxmlLoader.load(), 322.0, 391.0);
                    Stage stage = new Stage();
                    stage.setTitle("Sign Up Page");
                    stage.setScene(scene);
                    stage.show();
                    ((Node)(event.getSource())).getScene().getWindow().hide();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }catch(IOException | ParseException e){
            e.printStackTrace();
        }
    }

    /**
     * Handles the action of the log in button being pressed
     * Takes user to the log in screen
     * @param event
     */
    private void login(ActionEvent event) {
        try {
            // Switches to login screen
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("logGUI.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 322.0, 391.0);
            Stage stage = new Stage();
            stage.setTitle("Login Page");
            stage.setScene(scene);
            stage.show();
            ((Node)(event.getSource())).getScene().getWindow().hide();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle rb){
        button_signup.setOnAction(this::submit);
        button_log_in.setOnAction(this::login);

    }
}
