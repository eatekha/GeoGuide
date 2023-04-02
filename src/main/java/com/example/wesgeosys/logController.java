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

public class logController implements Initializable {
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
     * The button used by the user to login
     */
    @FXML
    public Button button_login;
    /**
     * The button used by the user to go to the sign up screen
     */
    @FXML
    Button button_sign_up;
    /**
     * The account database used to check if the login is valid
     */
    accountClass num1 = new accountClass("src/main/java/com/example/wesgeosys/accountData.json");
    /**
     * The boolean value for if the username is valid
     */
    boolean validUser = false;
    /**
     * The boolean value for if the password is valid
     */
    boolean validPw = false;
    /**
     * The boolean value for if the account is an admin account
     */
    boolean admin = false;

    /**
     * Handles the action of the login button being pressed.
     * If login is valid with admin permissions, taken to admin view
     * If login is valid with no admin permissions, taken to MainMaps view
     * If login is invalid, taken to invalid login screen where they can try again
     * @param event
     */
    private void submit(ActionEvent event){
        try {
            // Checks if given text is equal to a username found in JSON file
            if(tf_username.getText().equals(num1.getUser(tf_username.getText()))){
                validUser = true;
            }
            // Checks if given text is equal to the password associated with given user
            if (tf_password.getText().equals(num1.getPassword(tf_username.getText()))){
                validPw = true;
            }
            // If account has admin permissions, sets admin to true;
            admin = num1.checkAdmin(tf_username.getText());
            // If the username and password is valid, and account is an admin account
            if(validUser && validPw && admin){
                // Switches to Admin View
                try {
                    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("admMapsGUI.fxml"));
                    String username = tf_username.getText();
                    mainMapsController.username = username;
                    mainMapsController.adminPermissions = admin;
                    Scene scene = new Scene(fxmlLoader.load(), 805.0, 1398.0);
                    Stage stage = new Stage();
                    stage.setTitle("Adminstration Maps");
                    stage.setScene(scene);
                    stage.setMaximized(true);
                    stage.show();
                    ((Node)(event.getSource())).getScene().getWindow().hide();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }else if(validUser && validPw){
                // Switches to Admin View
                try {
                    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("mainMapsGUI.fxml"));
                    mainMapsController.username = tf_username.getText();
                    mainMapsController.adminPermissions = admin;
                    Scene scene = new Scene(fxmlLoader.load(), 1398.0, 805.0);
                    Stage stage = new Stage();
                    stage.setTitle("Main User Maps");
                    stage.setScene(scene);
                    stage.setMaximized(true);
                    stage.show();
                    ((Node)(event.getSource())).getScene().getWindow().hide();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }else{
                // Switches to Invalid Login Screen
                try {
                    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("logInvalidGUI.fxml"));
                    Scene scene = new Scene(fxmlLoader.load(), 322.0, 391.0);
                    Stage stage = new Stage();
                    stage.setTitle("Login Invalid");
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
     * Handles the action of the sign up button being pressed
     * Takes the user to the sign up screen
     * @param event
     */
    private void signUp(ActionEvent event) {
        // Switches to Sign Up screen
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("signupGUI.fxml"));
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
    @Override
    public void initialize(URL url, ResourceBundle rb){
        button_login.setOnAction(this::submit);
        button_sign_up.setOnAction(this::signUp);
    }
}
