package com.example.wgis;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;

public class addPOIPage extends Application{
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(addPOIPage.class.getResource("addPOIPage.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 322, 391);
        stage.setTitle("Add POI");
        stage.setScene(scene);
        stage.show();
    }
}
