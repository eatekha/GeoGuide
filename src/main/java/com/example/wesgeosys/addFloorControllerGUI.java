package com.example.wesgeosys;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class addFloorControllerGUI extends Application {
    /**
     * Initializes and displays the Add Floor GUI by loading the addFloorGUI.fxml file using FXMLLoader
     * Sets the title, scene, and stage properties before displaying the GUI
     *
     * @param stage The primary stage of the JavaFX application
     * @throws IOException If there is an error loading the addFloorGUI.fxml file
     */
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(addFloorControllerGUI.class.getResource("addFloorGUI.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 322.0, 391.0);;
        stage.setTitle("Add Floor");
        stage.setScene(scene);
        stage.show();
    }
}