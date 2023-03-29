package com.example.wgis;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;

public class editFloorPage extends Application{
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(editFloorPage.class.getResource("editFloorPage.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 322, 391);
        stage.setTitle("Edit Floor");
        stage.setScene(scene);
        stage.show();
    }
}
