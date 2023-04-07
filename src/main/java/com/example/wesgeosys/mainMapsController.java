package com.example.wesgeosys;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

import static com.example.wesgeosys.addBuildingController.bname;
import static com.example.wesgeosys.addPOIController.newpname;
import static com.example.wesgeosys.editPOIController.*;

/**
 * The controller for the main maps view.
 */
public class mainMapsController {
    /**
     * A list of all image icons used on the map.
     */
    private static List<ImageView> imageIcons = new ArrayList<>();

    @FXML
    private AnchorPane adminPanel;
    @FXML
    private ComboBox favDropdown;
    @FXML
    private ComboBox floorsDropdown;
    @FXML
    private ImageView mapDisplay;
    @FXML
    private ComboBox layersDropdown;
    @FXML
    private ComboBox mapsDropdown;
    @FXML
    private ComboBox poiDropdown;

    /**
     * The file path to the map images.
     */
    private String mapFilePath = "src/main/java/com/example/wesgeosys/mapImages/";
    /**
     * The index of the current floor.
     */
    private int currentFloorIndex;

    public accountClass user;
    public JSONArray buildingDataFile;
    public JSONObject currentBuildingData;
    public JSONObject currentFloor;
    public JSONArray currentPOIList;
    public JSONObject currentPOI;
    public editTool editHelper;
    public static Boolean adminAccess;
    public static String username;
    public JSONArray userFileData;
    public JSONObject userInstance;
    public searchHelperTool searchUtility;

    @FXML
    private Label currentTemperature;
    @FXML
    private Label descriptionText;
    @FXML
    private Label feelsLikeTemperature;
    @FXML
    private Label highTemperature;
    @FXML
    private Label lowTemperature;

    EventHandler<ActionEvent> favDropdownHandler;
    EventHandler<ActionEvent> floorsDropdownHandler;
    EventHandler<ActionEvent> layerDropdownHandler;
    EventHandler<ActionEvent> poiDropdownHandler;

    private Stage popupPane;

    /**
     * Whether or not an add POI icon is active.
     */
    boolean addPOIIcon = false;
    /**
     * Whether or not a placeable icon is active.
     */
    boolean placeableIcon = false;
    /**
     * The currently placed icon.
     */
    ImageView placedIcon;

    double mapOffsetX = 140;
    double mapOffsetY = 10;
    double mapSizeX = 1398;
    double mapSizeY = 805;
    double xCoordinate;
    double yCoordinate;

    /**
     * Logs out the current user and displays the login page.
     *
     * @throws IOException If an I/O error occurs when opening the login page.
     */
    @FXML
    protected void handleLogout() throws IOException {
        try {
            Stage stage = (Stage) adminPanel.getScene().getWindow();
            stage.close();
            FXMLLoader fxmlLoader = new FXMLLoader(logControllerGUI.class.getResource("logGUI.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 391.0, 322.0);
            Stage newLogin = new Stage();
            newLogin.setTitle("Login Page");
            newLogin.setScene(scene);
            newLogin.show();
        } catch (Exception e) {
            displayError(e);
        }
    }

    /**
     * Displays the help pop-up window with information on how to use the application.
     */
    @FXML
    protected void handleOpenHelp() {
        try {
            popupPane = new Stage();
            Parent root = new FXMLLoader(mainMapsController.class.getResource("helpPopGUI.fxml")).load();
            Scene scene = new Scene(root, 625, 500);
            popupPane.setTitle("Help");
            popupPane.setScene(scene);
            popupPane.show();
        } catch (Exception e) {
            displayError(e);
        }
    }

    /**
     * Handles the action triggered when a point of interest (POI) is selected.
     * Clears all icons, disables the layers and favorites dropdown, sets their values to empty, and re-enables them.
     * Finds the selected POI's index in the current POI list and sets the current POI to the one at that index.
     * Loads and displays the icon image corresponding to the selected POI's layer type, and adds it to the admin panel.
     * If the selected POI has a description, sets the description text to it.     *
     *
     * @throws FileNotFoundException if the icon image file for the selected POI cannot be found
     */
    @FXML
    protected void handlePOIAction() {
        clearAllIcons();
        layersDropdown.setOnAction(null);
        favDropdown.setOnAction(null);
        layersDropdown.setValue("");
        favDropdown.setValue("");
        layersDropdown.setOnAction(layerDropdownHandler);
        favDropdown.setOnAction(favDropdownHandler);
        if (poiDropdown.getValue() != null) {
            String string = poiDropdown.getValue().toString();
            String[] poiData = string.split(":");
            int indexVal = searchUtility.getPointOfInterestIndex(currentPOIList, poiData[0], poiData[1]);
            currentPOI = (JSONObject) currentPOIList.get(indexVal);
            String layerType = currentPOI.get("layerType").toString();
            try {
                String imageName = "Icon Image - " + layerType + ".png";
                String imagePath = "src/main/java/com/example/wesgeosys/iconImages/" + imageName;
                ImageView displayImage = new ImageView(new Image(new FileInputStream(imagePath)));
                displayImage.setPreserveRatio(true);
                displayImage.setFitWidth(30);
                displayImage.setX(((searchUtility.getCoordinates("X", (JSONObject) currentPOI) / 3400.0) * mapSizeX) + mapOffsetX - 15.0);
                displayImage.setY(((searchUtility.getCoordinates("Y", (JSONObject) currentPOI) / 2200.0) * mapSizeY) + mapOffsetY - 15.0);
                adminPanel.getChildren().add(displayImage);
                imageIcons.add(displayImage);
                Object desc = currentPOI.get("description");
                if (desc != null) {
                    descriptionText.setText(desc.toString());
                }
            } catch (FileNotFoundException e) {
                displayError(e);
            }
        }
    }

    /**
     * Handles the event when a favorite point of interest is selected.
     * Clears all the existing icons and sets the event handlers for the layers and poi dropdowns.
     * Gets the current point of interest based on the selected favorite and its index in the current point of interest list.
     * Sets the icon image based on the layer type of the current point of interest and displays it on the admin panel.
     *
     * @throws FileNotFoundException if the icon image file is not found
     */
    @FXML
    protected void handleFavAction() {
        clearAllIcons();
        layersDropdown.setOnAction(null);
        poiDropdown.setOnAction(null);
        layersDropdown.setValue("");
        poiDropdown.setValue("");
        layersDropdown.setOnAction(layerDropdownHandler);
        poiDropdown.setOnAction(poiDropdownHandler);
        if (favDropdown.getValue() != null) {
            String string = favDropdown.getValue().toString();
            String[] poiData = string.split(":");
            int valueIndex = searchUtility.getPointOfInterestIndex(currentPOIList, poiData[0], poiData[1]);
            currentPOI = (JSONObject) currentPOIList.get(valueIndex);
            String layerType = currentPOI.get("layerType").toString();
            try {
                String imageName = "Icon Image - " + layerType + ".png";
                String imagePath = "src/main/java/com/example/wesgeosys/iconImages/" + imageName;
                ImageView displayImage = new ImageView(new Image(new FileInputStream(imagePath)));
                displayImage.setPreserveRatio(true);
                displayImage.setFitWidth(30);
                displayImage.setX(((searchUtility.getCoordinates("X", (JSONObject) currentPOI) / 3400.0) * mapSizeX) + mapOffsetX - 15.0);
                displayImage.setY(((searchUtility.getCoordinates("Y", (JSONObject) currentPOI) / 2200.0) * mapSizeY) + mapOffsetY - 15.0);
                adminPanel.getChildren().add(displayImage);
                imageIcons.add(displayImage);
            } catch (FileNotFoundException e) {
                displayError(e);
            }
        }
    }

    /**
     * Handles the action when the user selects a layer from the layers dropdown menu.
     * Clears all icons and resets dropdown menus for poi and fav.
     * Sets the value of the poi and fav dropdown menus to empty strings.
     * Resets the event handlers for poi and fav dropdown menus.
     * Determines the layer type based on the selected value from the layers dropdown menu.
     * Retrieves the list of layers for the current floor and the selected layer type.
     * Adds the corresponding icon images to the admin panel based on the coordinates of each layer.
     *
     * @throws FileNotFoundException if the icon image file for the selected layer type cannot be found
     */
    @FXML
    protected void handleLayersAction() {
        clearAllIcons();
        poiDropdown.setOnAction(null);
        favDropdown.setOnAction(null);
        poiDropdown.setValue("");
        favDropdown.setValue("");
        poiDropdown.setOnAction(poiDropdownHandler);
        favDropdown.setOnAction(favDropdownHandler);
        String layerType = layersDropdown.getValue() != null ? layersDropdown.getValue().toString() : "Default";
        JSONArray layerList = searchUtility.findAllLayerKind(currentFloor, layerType);
        try {
            for (Object obj : layerList) {
                JSONObject layer = (JSONObject) obj;
                String imageName = "Icon Image - " + layerType + ".png";
                String imagePath = "src/main/java/com/example/wesgeosys/iconImages/" + imageName;
                ImageView displayView = new ImageView(new Image(new FileInputStream(imagePath)));
                displayView.setPreserveRatio(true);
                displayView.setFitWidth(30);
                displayView.setX(((searchUtility.getCoordinates("X", layer) / 3400.0) * mapSizeX) + mapOffsetX - 15.0);
                displayView.setY(((searchUtility.getCoordinates("Y", layer) / 2200.0) * mapSizeY) + mapOffsetY - 15.0);
                adminPanel.getChildren().add(displayView);
                imageIcons.add(displayView);
            }
        } catch (FileNotFoundException e) {
            displayError(e);
        }
    }


    /**
     * Handles the action when the user selects a new floor from the dropdown list.
     * Updates the current floor index, loads the corresponding floor map image, resets the POI dropdown list,
     * updates the list with POIs on the selected floor, and triggers the layers dropdown action handler.
     *
     * @throws FileNotFoundException if the map image file for the selected layer type cannot be found
     */
    @FXML
    protected void handleFloorsAction() {
        this.currentFloorIndex = Integer.parseInt(floorsDropdown.getValue().toString()) - 1;
        JSONArray temporaryArray = (JSONArray) currentBuildingData.get("floors");
        currentFloor = (JSONObject) temporaryArray.get(currentFloorIndex);
        String imageName = searchUtility.searchImage(currentBuildingData.get("Building").toString(), Integer.parseInt(floorsDropdown.getValue().toString()) - 1);
        try {
            mapDisplay.setImage(new Image(new FileInputStream(mapFilePath + imageName)));
            resetComboBox(poiDropdown);
            JSONObject temporaryObject = (JSONObject) temporaryArray.get(currentFloorIndex);
            currentPOIList = (JSONArray) temporaryObject.get("pointsOfInterest");
            for (int n = 0; n < currentPOIList.size(); n++) {
                temporaryObject = (JSONObject) currentPOIList.get(n);
                poiDropdown.getItems().add(temporaryObject.get("name") + ":" + temporaryObject.get("roomNum"));
            }
            handleLayersAction();
        } catch (FileNotFoundException e) {
            System.out.println("Error: File Not Found");
        }
    }

    /**
     * Handles the toggle favourites action. If a point of interest is currently selected and it is marked as a favourite,
     * it will be unmarked as a favourite and removed from the favourites dropdown. If a point of interest is currently selected
     * and it is not marked as a favourite, it will be marked as a favourite and added to the favourites dropdown. If no point of
     * interest is currently selected, it will print a message indicating that a point of interest is not selected.
     */
    @FXML
    protected void handleToggleFavourites() {
        if (currentPOI == null) {
            System.out.println("POI NOT SELECTED");
            return;
        }
        boolean isFavourite = currentPOI.get("favourite").equals(true);
        editHelper.favouriteToggle(currentBuildingData.get("Building").toString(), currentFloorIndex, currentPOI, !isFavourite);
        if (isFavourite) {
            favDropdown.getItems().remove(currentPOI);
        } else {
            String favDropdownItem = (currentPOI.get("builtInPOI").equals(true))
                    ? currentPOI.get("name") + ":" + currentPOI.get("roomNum")
                    : "(User)" + currentPOI.get("name") + ":" + currentPOI.get("roomNum");
            favDropdown.getItems().add(favDropdownItem);
        }
    }


    /**
     * Handles the action when the user selects a map from the maps dropdown menu.
     * Clears all icons and resets dropdown menus for layers, poi and fav.
     * Sets the value of the layers, poi, and fav dropdown menus to empty strings.
     * Resets the event handlers for layers, poi, and fav dropdown menus.
     * Retrieves the image for the selected map and displays it on the map display.
     * Retrieves the current building data and the current floor data for the selected map.
     * Populates the floors dropdown menu with the number of floors available for the selected map.
     * Populates the poi dropdown menu with the points of interest for the first floor of the selected map.
     * Calls the handleLayersAction method to display the appropriate icons for the selected floor.
     *
     * @throws FileNotFoundException if the image for the selected map cannot be found
     */
    @FXML
    protected void handleMapsAction() {
        clearAllIcons();
        layersDropdown.setOnAction(null);
        poiDropdown.setOnAction(null);
        favDropdown.setOnAction(null);
        layersDropdown.setValue("");
        poiDropdown.setValue("");
        favDropdown.setValue("");
        layersDropdown.setOnAction(floorsDropdownHandler);
        poiDropdown.setOnAction(poiDropdownHandler);
        favDropdown.setOnAction(favDropdownHandler);
        String imageName = searchUtility.searchImage(mapsDropdown.getValue().toString(), 0);
        this.currentBuildingData = searchUtility.getBuildingObject(mapsDropdown.getValue().toString());
        JSONArray temporaryArray = (JSONArray) currentBuildingData.get("floors");
        this.currentFloor = (JSONObject) temporaryArray.get(0);
        try {
            mapDisplay.setImage(new Image(new FileInputStream(mapFilePath + imageName)));
            int val = poiDropdown.getItems().size();
            for (int initialVal = 0; initialVal < val; initialVal++) {
                poiDropdown.getItems().remove(0);
            }
            floorsDropdown.getItems().clear();
            for (int initialVal = 1; initialVal <= temporaryArray.size(); initialVal++) {
                floorsDropdown.getItems().add(initialVal);
            }
            floorsDropdown.setValue("1");
            JSONObject temporaryObject = (JSONObject) temporaryArray.get(0);
            currentPOIList = (JSONArray) temporaryObject.get("pointsOfInterest");
            poiDropdown.getItems().clear();
            for (int initialVal2 = 0; initialVal2 < currentPOIList.size(); initialVal2++) {
                temporaryObject = (JSONObject) currentPOIList.get(initialVal2);
                if ((Boolean) temporaryObject.get("builtInPOI")) {
                    poiDropdown.getItems().add(temporaryObject.get("name") + ":" + temporaryObject.get("roomNum"));
                } else {
                    poiDropdown.getItems().add("(User)" + temporaryObject.get("name") + ":" + temporaryObject.get("roomNum"));
                }
            }
            handleLayersAction();
        } catch (FileNotFoundException e) {
            displayError(e);
        } finally {
            floorsDropdown.setOnAction(floorsDropdownHandler);
        }
    }

    protected void mergeJSON() {
        JSONArray userPOIs = (JSONArray) userInstance.get("userPOIs");
        JSONArray favList = (JSONArray) userInstance.get("favourites");
        JSONObject usertemporaryObject;
        JSONObject builttemporaryObject;
        JSONArray temporaryArray;
        JSONArray poiList;
        for (int initialVal = 0; initialVal < userPOIs.size(); initialVal++) {
            usertemporaryObject = (JSONObject) userPOIs.get(initialVal);
            for (int initialVal2 = 0; initialVal2 < buildingDataFile.size(); initialVal2++) {
                builttemporaryObject = (JSONObject) buildingDataFile.get(initialVal2);
                if (usertemporaryObject.get("building").equals(builttemporaryObject.get("Building").toString())) {
                    temporaryArray = (JSONArray) builttemporaryObject.get("floors");
                    for (int initialVal3 = 0; initialVal3 < temporaryArray.size(); initialVal3++) {
                        builttemporaryObject = (JSONObject) temporaryArray.get(initialVal3);
                        if (Integer.parseInt(usertemporaryObject.get("floorNum").toString()) == initialVal3) {
                            poiList = (JSONArray) builttemporaryObject.get("pointsOfInterest");
                            editHelper.addPOI(poiList);
                        }
                    }
                }
            }
        }
        for (int k = 0; k < favList.size(); k++) {
            usertemporaryObject = (JSONObject) favList.get(k);
            for (int n = 0; n < buildingDataFile.size(); n++) {
                builttemporaryObject = (JSONObject) buildingDataFile.get(0);
                if (usertemporaryObject.get("building").equals(builttemporaryObject.get("Building").toString())) {
                    temporaryArray = (JSONArray) builttemporaryObject.get("floors");
                    for (int f = 0; f < temporaryArray.size(); f++) {
                        builttemporaryObject = (JSONObject) temporaryArray.get(f);
                        if (Integer.parseInt(usertemporaryObject.get("floorNum").toString()) == f) {
                            poiList = (JSONArray) builttemporaryObject.get("pointsOfInterest");
                            for (int p = 0; p < poiList.size(); p++) {
                                JSONObject curPoi = (JSONObject) poiList.get(p);
                                if (curPoi.get("name").equals(usertemporaryObject.get("name")) && curPoi.get("roomNum").equals(usertemporaryObject.get("roomNum"))) {
                                    editHelper.favouriteToggle(currentBuildingData.get("Building").toString(), currentFloorIndex, curPoi, true);
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    @FXML
    protected void insertBuilding() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("addBuildingGUI.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 322.0, 391.0);
            Stage stage = new Stage();
            stage.setTitle("Add Building");
            stage.setScene(scene);
            stage.showAndWait();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        editHelper.addBuilding(bname);
        mapsDropdown.getItems().add(bname);
    }

    @FXML
    protected void modifyBuilding() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("editBuildingGUI.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 322.0, 391.0);
            Stage stage = new Stage();
            stage.setTitle("Edit Building");
            stage.setScene(scene);
            stage.showAndWait();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        editHelper.editBuilding(currentBuildingData, bname);
    }

    @FXML
    protected void deleteBuilding() {
        editHelper.removeBuilding((String) currentBuildingData.get("Building"));
        mapsDropdown.getItems().remove(currentBuildingData.get("Building"));
        currentBuildingData = (JSONObject) buildingDataFile.get(0);
        JSONArray temporaryArray = (JSONArray) currentBuildingData.get("floors");
        currentFloor = (JSONObject) temporaryArray.get(0);
        String imageName = currentFloor.get("imageFileName").toString();
        try {
            mapDisplay.setImage(new Image(new FileInputStream(mapFilePath + imageName)));
        } catch (FileNotFoundException e) {
            System.out.println("File not found");
        }
    }

    @FXML
    protected void insertPOI() {
        displayAlert();
        addPOIIcon = true;
        JSONObject temporaryObject;
        if (adminAccess) {
            editHelper.addPOI(currentPOIList);
            temporaryObject = (JSONObject) currentPOIList.get(0);
            poiDropdown.getItems().add(temporaryObject.get("name") + ":" + temporaryObject.get("roomNum"));
        } else {
            editHelper.addPOI(currentPOIList);
            editHelper.addPOI((JSONArray) userInstance.get("userPOIs"));
            editHelper.addToUserPOI(currentBuildingData.get("Building").toString(), currentFloorIndex);
            JSONArray temporaryArray = (JSONArray) userInstance.get("userPOIs");
            temporaryObject = (JSONObject) temporaryArray.get(0);
            poiDropdown.getItems().add("(User)" + temporaryObject.get("name") + ":" + temporaryObject.get("roomNum"));
        }
    }

    @FXML
    protected void modifyPOI() throws IOException {
        if (clearPOI()) {
            return;
        } else {
            displayAlert();
            placeableIcon = true;
        }
    }

    private void displayAlert() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("alertPageGUI.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 600, 300);
            Stage stage = new Stage();
            stage.setTitle("Alert Page");
            stage.setScene(scene);
            stage.showAndWait();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void modifyPOIPopout() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("editPOIGUI.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 322.0, 391.0);
            Stage stage = new Stage();
            stage.setTitle("Edit POI");
            stage.setScene(scene);
            stage.showAndWait();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        if (currentPOI != null) {
            String nameInput = currentPOI.get("name").toString();
            String descriptionInput = currentPOI.get("description").toString();
            String roomNumInput = currentPOI.get("roomNum").toString();
            String layerTypeInput = currentPOI.get("layerType").toString();
            int xInput = Integer.parseInt(currentPOI.get("xCord").toString());
            int yInput = Integer.parseInt(currentPOI.get("yCord").toString());
            int newX = (int) xCoordinate;
            int newY = (int) yCoordinate;
            System.out.println(newX);
            System.out.println(newY);
            System.out.println(pname);
            System.out.println(pdesc);
            System.out.println(proom);
            System.out.println(player);
            editHelper.editPOI(currentPOI, pname, pdesc, newX, newY, proom, player);
            resetComboBox(poiDropdown);
            JSONObject temporaryObject;
            for (int n = 0; n < currentPOIList.size(); n++) {
                temporaryObject = (JSONObject) currentPOIList.get(n);
                poiDropdown.getItems().add(temporaryObject.get("name") + ":" + temporaryObject.get("roomNum"));
            }
        } else {
            System.out.println("No Current POI Selected");
        }
        adminPanel.getChildren().remove(placedIcon);
        placeableIcon = false;
    }

    @FXML
    protected void deletePOI() {
        if (clearPOI()) {
        } else {
            if (currentPOI != null) {
                clearAllIcons();
                if (adminAccess) {
                    editHelper.removePOI(currentPOIList, currentPOI);
                    poiDropdown.getItems().remove(currentPOI.get("name") + ":" + currentPOI.get("roomNum"));
                    currentPOI = null;
                } else {
                    editHelper.removePOI(currentPOIList, currentPOI);
                    editHelper.removePOI((JSONArray) userInstance.get("userPOIs"), currentPOI);
                    editHelper.removeUserPOI(currentPOI);
                    poiDropdown.getItems().remove("(User)" + currentPOI.get("name") + ":" + currentPOI.get("roomNum"));
                    currentPOI = null;
                }
            } else {
                System.out.println("No POI selected");
            }
        }

    }

    @FXML
    protected void insertFloor() {
        editHelper.addFloor(currentBuildingData, "DefaultFile.png");
        floorsDropdown.getItems().add(floorsDropdown.getItems().size() + 1);
    }

    @FXML
    protected void deleteFloor() {
        JSONArray temporaryArray = (JSONArray) currentBuildingData.get("floors");
        editHelper.removeFloor(temporaryArray, currentFloorIndex);
        floorsDropdown.getItems().remove(floorsDropdown.getItems().size() - 1);
        temporaryArray = (JSONArray) currentBuildingData.get("floors");
        currentFloor = (JSONObject) temporaryArray.get(0);
        try {
            mapDisplay.setImage(new Image(new FileInputStream(mapFilePath + currentFloor.get("imageFileName").toString())));
        } catch (FileNotFoundException e) {
            System.out.println("File not found");
        }
    }

    private void clearAllIcons() {
        for (ImageView icon : imageIcons) {
            adminPanel.getChildren().remove(icon);
        }
    }

    private void resetComboBox(ComboBox cBox) {
        int val = cBox.getItems().size();
        for (int n = 0; n < val; n++) {
            cBox.getItems().remove(0);
        }
    }

    private boolean clearPOI() {
        if (poiDropdown.getValue() == null) {
            return true;
        }
        return false;
    }

    @FXML
    public void initialize() throws FileNotFoundException {
        floorsDropdownHandler = floorsDropdown.getOnAction();
        poiDropdownHandler = poiDropdown.getOnAction();
        favDropdownHandler = favDropdown.getOnAction();
        layerDropdownHandler = layersDropdown.getOnAction();
        floorsDropdown.setValue("1");
        mapDisplay.setOnMouseClicked(e -> {
            try {
                handleMouseDown(e);
            } catch (FileNotFoundException ex) {
                throw new RuntimeException(ex);
            }
        });

        this.currentFloorIndex = 0;
        JSONParser jsonParser = new JSONParser();
        try (FileReader reader = new FileReader("src/main/java/com/example/wesgeosys/backUpBuiltInPOI.json")) {
            try (FileReader accountReader = new FileReader("src/main/java/com/example/wesgeosys/accountData.json")) {
                JSONObject currentBuilding = (JSONObject) jsonParser.parse(reader);
                searchUtility = new searchHelperTool((JSONArray) currentBuilding.get("buildings"), adminAccess);
                this.buildingDataFile = (JSONArray) currentBuilding.get("buildings");
                this.currentBuildingData = (JSONObject) buildingDataFile.get(0);
                JSONArray temporaryArray = (JSONArray) currentBuildingData.get("floors");
                this.currentFloor = (JSONObject) temporaryArray.get(currentFloorIndex);
                searchHelperTool search = new searchHelperTool(buildingDataFile, adminAccess);
                this.editHelper = new editTool(buildingDataFile, username, adminAccess);
                this.userFileData = (JSONArray) jsonParser.parse(accountReader);
                for (int n = 0; n < userFileData.size(); n++) {
                    JSONObject temporaryObject = (JSONObject) userFileData.get(n);
                    if (temporaryObject.get("username") == null) {
                    } else if (temporaryObject.get("username").toString().equals(username)) {
                        this.userInstance = temporaryObject;
                    }
                }
                mergeJSON();
                this.currentPOIList = (JSONArray) currentFloor.get("pointsOfInterest");
                for (int n = 0; n < buildingDataFile.size(); n++) {
                    mapsDropdown.getItems().add(search.getBuildingIndex(n));
                }
                for (int n = 0; n < currentPOIList.size(); n++) {
                    JSONObject tmp = (JSONObject) currentPOIList.get(n);
                    if ((Boolean) tmp.get("builtInPOI")) {
                        poiDropdown.getItems().add(tmp.get("name") + ":" + tmp.get("roomNum"));
                    } else {
                        poiDropdown.getItems().add("(User)" + tmp.get("name") + ":" + tmp.get("roomNum"));
                    }
                }
                temporaryArray = (JSONArray) userInstance.get("favourites");
                JSONObject temporaryObject;
                for (int n = 0; n < temporaryArray.size(); n++) {
                    temporaryObject = (JSONObject) temporaryArray.get(n);
                    if (temporaryObject.get("builtInPOI").equals(true)) {
                        favDropdown.getItems().add(temporaryObject.get("name") + ":" + temporaryObject.get("roomNum"));
                    } else {
                        favDropdown.getItems().add("(User)" + temporaryObject.get("name") + ":" + temporaryObject.get("roomNum"));
                    }
                }
                layersDropdown.getItems().addAll("Classroom", "CollaborationSpace", "Elevator", "Lab", "Navigation", "Washroom");
                floorsDropdown.getItems().addAll("1", "2", "3", "4", "5");
                mapsDropdown.setValue(search.getBuildingIndex(0));
            } catch (ParseException e) {
                System.out.println("Parse Exception");
            }
        } catch (IOException e) {

        }
        weatherReport.setAllWeatherData();
        currentTemperature.setText(weatherReport.getCurrentTemperature() + " °C");
        feelsLikeTemperature.setText(weatherReport.getFeelsLikeTemperature() + " °C");
        lowTemperature.setText(weatherReport.getMinTemperature() + " °C");
        highTemperature.setText(weatherReport.getMaxTemperature() + " °C");
    }

    @FXML
    private void handleMouseDown(MouseEvent e) throws FileNotFoundException {
        xCoordinate = e.getX() / mapSizeX * 3400;
        yCoordinate = e.getY() / mapSizeY * 2200;
        if (!placeableIcon && !addPOIIcon) {
            return;
        }
        try {
            ImageView imgView = new ImageView(new Image(new FileInputStream("src/main/java/com/example/wesgeosys/iconImages/Icon Image - Placeholder.png")));
            imgView.setPreserveRatio(true);
            imgView.setX(e.getX() + mapOffsetX - 15);
            imgView.setY(e.getY() + mapOffsetY - 15);
            imgView.setFitWidth(30);
            placedIcon = imgView;
            adminPanel.getChildren().add(imgView);
            imageIcons.add(imgView);
            if (placeableIcon) {
                modifyPOIPopout();
            }
            if (addPOIIcon) {
                insertPOIPopout();
            }
        } catch (Exception error) {
            displayError(error);
        }
    }

    private void insertPOIPopout() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("addPOIGUI.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 322.0, 391.0);
            Stage stage = new Stage();
            stage.setTitle("Add POI");
            stage.setScene(scene);
            stage.showAndWait();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        int newX = (int) xCoordinate;
        int newY = (int) yCoordinate;
        System.out.println(newpname);
        adminPanel.getChildren().remove(placedIcon);
        addPOIIcon = false;
    }

    private void displayError(Exception e) {
        System.out.println("Error occurred: " + e);
    }
}
