package com.example.wesgeosys;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;

public class admMapsControllerGUI extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(addFloorControllerGUI.class.getResource("admMapsGUI.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 805.0, 1398.0);
        stage.setTitle("Admin Maps");
        stage.setScene(scene);
        stage.show();
    }
}