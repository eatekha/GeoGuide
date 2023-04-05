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

    @FXML
    private TextField passwordField;

    @FXML
    private TextField usernameField;

    @FXML
    public Button button_log_in;

    @FXML
    Button button_signup;

    accountClass num1 = new accountClass("src/main/java/com/example/wesgeosys/accountData.json");

    boolean validUser = false;

    private void submit(ActionEvent event) {
        try{
            if(num1.checkValidUsername(usernameField.getText())){
                validUser = true;
            }
            if(validUser){
                try {
                    num1.createAccount(usernameField.getText(), passwordField.getText());
                    mainMapsController.username = usernameField.getText();
                    mainMapsController.adminPermissions = false;
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
            }else{
                try {
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
