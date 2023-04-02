package com.example.wesgeosys;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class logControllerGUI extends Application {
    /**
     * Creates the stage using the login.fxml file
     * @param stage
     * @throws IOException
     */
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(logControllerGUI.class.getResource("logGUI.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 322.0, 391.0);;
        stage.setTitle("Login Page");
        stage.setScene(scene);
        stage.show();
    }
}