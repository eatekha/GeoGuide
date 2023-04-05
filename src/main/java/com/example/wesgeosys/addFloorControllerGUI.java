package com.example.wesgeosys;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class addFloorControllerGUI extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(addFloorControllerGUI.class.getResource("addFloorGUI.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 322.0, 391.0);;
        stage.setTitle("Add Floor");
        stage.setScene(scene);
        stage.show();
    }
}