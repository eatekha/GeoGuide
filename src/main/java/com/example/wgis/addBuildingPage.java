package com.example.wgis;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;

/**
 * The graphical user interface for the add building popout
 * Admins may add buildings from this popout
 */
public class addBuildingPage extends Application{
    /**
     * Creates the stage for the add building popout using the AddBuilding.fxml file
     * @param stage
     * @throws IOException
     */
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(addBuildingPage.class.getResource("addBuildingPage.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 322, 391);
        stage.setTitle("Add Building");
        stage.setScene(scene);
        stage.show();
    }
}
