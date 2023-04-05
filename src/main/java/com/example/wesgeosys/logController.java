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

    @FXML
    private TextField passwordField;

    @FXML
    private TextField usernameField;

    @FXML
    public Button loginButton;

    @FXML
    public Button signupButton;

    accountClass num1 = new accountClass("src/main/java/com/example/wesgeosys/accountData.json");

    boolean validUser = false;

    boolean validPw = false;

    boolean admin = false;


    private void submit(ActionEvent event){
        try {
            if(usernameField.getText().equals(num1.getUser(usernameField.getText()))){
                validUser = true;
            }
            if (passwordField.getText().equals(num1.getPassword(usernameField.getText()))){
                validPw = true;
            }
            admin = num1.checkAdmin(usernameField.getText());
            if(validUser && validPw && admin){
                try {
                    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("admMapsGUI.fxml"));
                    String username = usernameField.getText();
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
                    mainMapsController.username = usernameField.getText();
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


    private void signUp(ActionEvent event) {
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
        loginButton.setOnAction(this::submit);
        signupButton.setOnAction(this::signUp);
    }
}
