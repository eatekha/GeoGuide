package com.example.wesgeosys;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class addBuildingControllerGUI extends Application {
    /**
     * Initializes and shows the stage for adding a new building, using the AddBuilding.fxml file
     *
     * @param stage the stage to show the add building popout on
     * @throws IOException if there is an error reading the FXML file
     */
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(addBuildingControllerGUI.class.getResource("addBuildingGUI.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 322.0, 391.0);
        stage.setTitle("Add Building");
        stage.setScene(scene);
        stage.show();
    }
}
