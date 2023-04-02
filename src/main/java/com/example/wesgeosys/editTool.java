package com.example.wesgeosys;

import org.json.simple.JSONObject;
import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;

/**
 * Represents an editing tool that allows developers to easily edit metadata for built-in points of interest.
 * This tool enables users to manage building and point of interest metadata, including adding, editing, and removing
 * buildings.
 */
public class editTool {

    /**
     * Represents a group of constants to describe different types of Points of Interest (POIs) in a building.
     * The POI types are used to categorize and distinguish between different POIs.
     */
    enum layerType {
        Class,
        Lab,
        CollaborationSpace,
        Washroom,
        Elevator,
        Navigation,
        Default,
    }

    /**
     * Represents username of the user.
     */
    protected String username;
    /**
     * Represents permissions of the user.
     */
    protected boolean adminPermissions;
    /**
     * Represents JSON database of the buildings.
     */
    protected JSONArray buildingList;
    /**
     * An array containing all the user accounts in JSON format.
     */
    protected JSONArray accounts;
    /**
     * The name of the admin account, used for comparison and access control.
     */
    final String adminName = "Admin";

    /**
     * Constructor for the edit tool.
     * Reads in account data from a JSON file, and initializes instance variables.
     *
     * @param buildings JSONArray of building data
     * @param username  String representing current user's username
     * @param admin     Boolean representing whether current user has admin permissions
     */
    public editTool(JSONArray buildings, String username, Boolean admin) {
        JSONParser jsonParser = new JSONParser();
        try (FileReader reader = new FileReader("src/main/java/com/example/wesgeosys/accountData.json")) {
            this.accounts = (JSONArray) jsonParser.parse(reader);
        } catch (IOException e) {
            System.out.println("IOException");
        } catch (ParseException e) {
            System.out.println("ParseException");
        }
        this.adminPermissions = admin;
        this.buildingList = buildings;
        this.username = username;
    }

    /**
     * Adds a new building to the database of buildings and points of interest.
     * Only users with admin permissions are allowed to perform this operation.
     *
     * @param buildName the name of the building to be added
     */
    public void addBuilding(String buildName) {
        if (adminPermissions) {
            JSONObject newBuild = new JSONObject();
            JSONArray newFloor = createFloor();
            newBuild.put("Building", buildName);
            newBuild.put("floors", newFloor);
            buildingList.add(newBuild);
            saveData();
        } else {
            System.out.println("Invalid Permissions to add building");
        }
    }

    /**
     * Edits the name of a building in the database of buildings and points of interests.
     *
     * @param currentBuilding The JSONObject of the building to be edited.
     * @param newBuildName    The new name to be assigned to the building.
     */
    public void editBuilding(JSONObject currentBuilding, String newBuildName) {
        if (adminPermissions) {
            int num = findIndex(currentBuilding.get("Building").toString());
            buildingList.remove(num);
            currentBuilding.replace("Building", newBuildName);
            buildingList.add(currentBuilding);
            saveData();
        } else {
            System.out.println("Invalid Permission to edit building");
        }
    }

    /**
     * Removes a building from the database of buildings and points of interests.
     *
     * @param buildName the name of the building to be removed
     */
    public void removeBuilding(String buildName) {
        if (adminPermissions) {
            buildingList.remove(buildingList.get(findIndex(buildName)));
            saveData();
        } else {
            System.out.println("Invalid Permission to Remove Building");
        }
    }

    /**
     * Adds floor layer to the building in the database.
     *
     * @param imageName       string for the building name.
     * @param currentBuilding JSONObject for building.
     */
    public void addFloor(JSONObject currentBuilding, String imageName) {
        if (adminPermissions) {
            JSONObject newFloor = new JSONObject();
            JSONArray poiList = createPOI();
            newFloor.put("imageFileName", imageName);
            newFloor.put("pointsOfInterest", poiList);
            JSONArray tmpArray = (JSONArray) currentBuilding.get("floors");
            tmpArray.add(tmpArray.size(), newFloor);
            saveData();
        } else {
            System.out.println("Invalid Permissions to Add Floor");
        }
    }

    /**
     * Removes a point of interest from the database of buildings and points of interests.
     *
     * @param currentBuild JSONArray for the point of interest's name.
     * @param floorNum     of floor to be removed.
     */
    public void removeFloor(JSONArray currentBuild, int floorNum) {
        if (adminPermissions) {
            currentBuild.remove(floorNum);
            saveData();
        } else {
            System.out.println("Invalid Permissions to Remove Floor");
        }
    }

    /**
     * Adds a default point of interest to the building's database.
     *
     * @param poiList for the list of point of interests.
     *                A default POI will have default values for its properties such as layer type, visibility, and room number.
     *                If the user has admin permissions, the POI will be marked as built-in, otherwise it will not be.
     */
    public void addPOI(JSONArray poiList) {
        JSONObject defaultPOI = new JSONObject();
        defaultPOI.put("layerType", "Default");
        defaultPOI.put("visibility", true);
        defaultPOI.put("favourite", false);
        defaultPOI.put("description", "DefaultDescription");
        defaultPOI.put("name", "Default POI Name");
        defaultPOI.put("xCord", 0);
        defaultPOI.put("yCord", 0);
        defaultPOI.put("roomNum", "DefaultRoomNum");
        if (adminPermissions) {
            defaultPOI.put("builtInPOI", true);
        } else {
            defaultPOI.put("builtInPOI", false);
        }
        poiList.add(0, defaultPOI);
        saveData();
    }

    /**
     * Edits the details of a point of interest in the database of buildings and points of interests.
     * If the point of interest is a built-in POI, only an administrator account can edit it.
     * If the point of interest is a user-created POI, the user who created it can edit it.
     *
     * @param poi            the JSONObject representing the point of interest to edit.
     * @param newName        the new name for the point of interest.
     * @param newDescription the new description for the point of interest.
     * @param newX           the new x-coordinate for the point of interest.
     * @param newY           the new y-coordinate for the point of interest.
     * @param newRoomNum     the new room number for the point of interest.
     * @param newLayerType   the new layer type for the point of interest.
     */
    public void editPOI(JSONObject poi, String newName, String newDescription, int newX, int newY, String newRoomNum, String newLayerType) {
        if (poi.get("builtInPOI").equals(true)) {
            if (adminPermissions) {
                poi.replace("name", newName);
                poi.replace("description", newDescription);
                poi.replace("xCord", newX);
                poi.replace("yCord", newY);
                poi.replace("roomNum", newRoomNum);
                poi.replace("layerType", newLayerType);
                saveData();
            } else {
                System.out.println("You do not have the correct permissions to alter this point of interest");
            }
        } else {
            JSONObject userObj;
            for (int n = 0; n < accounts.size(); n++) {
                userObj = (JSONObject) accounts.get(n);
                String string = userObj.get("username").toString();
                if (string.equals(username)) {
                    JSONArray poiList = (JSONArray) userObj.get("userPOIs");
                    JSONObject tmpObj;
                    for (int k = 0; k < poiList.size(); k++) {
                        tmpObj = (JSONObject) poiList.get(k);
                        JSONObject newObj = new JSONObject();
                        if (tmpObj.get("name").equals(poi.get("name")) && tmpObj.get("roomNum").equals(poi.get("roomNum"))) {
                            poiList.remove(k);
                            poi.replace("building", poi.get("building"));
                            poi.replace("floorNum", poi.get("floorNum"));
                            poi.replace("name", newName);
                            poi.replace("description", newDescription);
                            poi.replace("xCord", newX);
                            poi.replace("yCord", newY);
                            poi.replace("roomNum", newRoomNum);
                            poi.replace("layerType", newLayerType);
                            poi.replace("builtInPOI", false);
                            poi.replace("favourite", poi.get("favourite"));
                            poiList.add(poi);
                            saveData();
                        }
                    }
                }
            }
        }
    }

    /**
     * Removes a point of interest from the database of buildings and points of interests.
     *
     * @param poiList    the JSONArray containing all the points of interest for a given user or building
     * @param currentPOI the JSONObject representing the point of interest to be removed
     */
    public void removePOI(JSONArray poiList, JSONObject currentPOI) {
        JSONObject tmpObj;
        for (int n = 0; n < poiList.size(); n++) {
            tmpObj = (JSONObject) poiList.get(n);
            if (tmpObj.get("name").equals(currentPOI.get("name")) && tmpObj.get("roomNum").equals(currentPOI.get("roomNum"))) {
                poiList.remove(n);
                saveData();
            }
        }
    }

    /**
     * Adds a default point of interest to the user's list of points of interest.
     *
     * @param buildName the name of the building for the default POI.
     * @param floorNum  the floor number for the default POI.
     */
    public void addToUserPOI(String buildName, int floorNum) {
        JSONObject userObj;
        for (int n = 0; n < accounts.size(); n++) {
            userObj = (JSONObject) accounts.get(n);
            String string = userObj.get("username").toString();
            if (string.equals(username)) {
                JSONArray poiList = (JSONArray) userObj.get("userPOIs");
                JSONObject defaultPOI = new JSONObject();
                defaultPOI.put("building", buildName);
                defaultPOI.put("floorNum", floorNum);
                defaultPOI.put("name", "Default POI Name");
                defaultPOI.put("description", "DefaultDescrip");
                defaultPOI.put("roomNum", "DefaultRoomNum");
                defaultPOI.put("layerType", "Default");
                defaultPOI.put("visibility", true);
                defaultPOI.put("favourite", false);
                defaultPOI.put("builtInPOI", false);
                defaultPOI.put("xCord", 0);
                defaultPOI.put("yCord", 0);
                poiList.add(0, defaultPOI);
                saveData();
            }
        }
    }

    /**
     * Toggles the "favourite" status of a point of interest in the user's favourites list.
     *
     * @param currentBuild The name of the building the point of interest is located in.
     * @param floorNum     The floor number the point of interest is located on.
     * @param currPOI      The JSONObject representing the point of interest to toggle.
     * @param favTF        The boolean value indicating whether the point of interest should be favourited or unfavourited.
     */
    public void favouriteToggle(String currentBuild, int floorNum, JSONObject currPOI, Boolean favTF) {
        JSONObject userObj;
        for (int n = 0; n < accounts.size(); n++) {
            userObj = (JSONObject) accounts.get(n);
            String string = userObj.get("username").toString();
            if (string.equals(username)) {
                JSONArray poiList = (JSONArray) userObj.get("favourites");
                poiList.remove(currPOI);
                currPOI.put("building", currentBuild);
                currPOI.put("floorNum", floorNum);
                currPOI.replace("favourite", favTF);
                poiList.add(currPOI);
                saveData();
            }
        }
    }

    /**
     * Removes a user-created point of interest from the user's list of POIs.
     *
     * @param poi The JSONObject representing the point of interest to be removed.
     */
    public void removeUserPOI(JSONObject poi) {
        JSONObject userObj;
        for (int n = 0; n < accounts.size(); n++) {
            userObj = (JSONObject) accounts.get(n);
            String string = userObj.get("username").toString();
            if (string.equals(username)) {
                JSONArray poiList = (JSONArray) userObj.get("userPOIs");
                JSONObject tmpObj;
                for (int k = 0; k < poiList.size(); k++) {
                    tmpObj = (JSONObject) poiList.get(k);
                    if (tmpObj.get("name").toString().equals(poi.get("name").toString())) {
                        poiList.remove(k);
                    }
                }
                saveData();
            }
        }
    }

    /**
     * Creates a new JSONArray for a floor with default information including an empty list of points of interest.
     * The created JSONArray is returned.
     *
     * @return The newly created JSONArray for a floor.
     */
    public JSONArray createFloor() {
        JSONArray floors = new JSONArray();
        JSONArray poiList = createPOI();
        JSONObject defaultFloorObj = new JSONObject();
        defaultFloorObj.put("imageFileName", "DefaultFileName");
        defaultFloorObj.put("pointsOfInterest", poiList);
        floors.add(0, defaultFloorObj);
        return floors;
    }

    /**
     * Creates a point of interest to add to the database of buildings and points of interests.
     *
     * @return poiList JSONArray that includes the new POI in database.
     */
    public JSONArray createPOI() {
        JSONArray poiList = new JSONArray();
        JSONObject defaultPOI = new JSONObject();
        defaultPOI.put("layerType", "Default");
        defaultPOI.put("visibility", true);
        defaultPOI.put("description", "Default");
        defaultPOI.put("name", "Unnamed Point of Interest");
        defaultPOI.put("xCord", 0);
        defaultPOI.put("yCord", 0);
        defaultPOI.put("roomNum", "");
        if (adminPermissions) {
            defaultPOI.put("builtInPOI", true);
        } else {
            defaultPOI.put("builtInPOI", false);
        }
        poiList.add(0, defaultPOI);
        return poiList;
    }

    /**
     * Finds the index of the building in the database of buildings and points of interests.
     *
     * @param buildName the name of the building
     * @return the index of the building, or -1 if it does not exist in the database
     */
    public int findIndex(String buildName) {
        for (int i = 0; i < buildingList.size(); i++) {
            JSONObject buildings = (JSONObject) buildingList.get(i);
            String buildingname = (String) buildings.get("Building");
            if (buildName.equals(buildingname)) {
                return i;
            }
        }
        System.out.println("The building does not exist.");
        return -1;
    }

    /**
     * Saves the current data to a JSON file depending on user permissions.
     */
    public void saveData() {
        try {
            if (adminPermissions) {
                BufferedWriter buffWriter = new BufferedWriter(new FileWriter("src/main/java/com/example/wesgeosys/builtInPOI.json"));
                JSONObject tmpObj = null;
                JSONArray tmpArray = new JSONArray();
                buffWriter.write("{\"buildings\":[");
                for (int n = 0; n < buildingList.size(); n++) {
                    tmpObj = (JSONObject) buildingList.get(n);
                    tmpArray = (JSONArray) tmpObj.get("floors");
                    buffWriter.write("{\"Building\":\"" + (String) tmpObj.get("Building") + "\",\"floors\":[");
                    for (int f = 0; f < tmpArray.size(); f++) {
                        JSONObject tmpObject = (JSONObject) tmpArray.get(f);
                        JSONArray poiList = (JSONArray) tmpObject.get("pointsOfInterest");
                        for (int k = 0; k < poiList.size(); k++) {
                            JSONObject checkObj = (JSONObject) poiList.get(k);
                            if ((Boolean) checkObj.get("builtInPOI") == true) {
                                buffWriter.write(poiList.get(k).toString());
                                if (f + 1 != tmpArray.size() || k + 1 != poiList.size()) {
                                    buffWriter.write(",");
                                }
                            }
                        }
                        if (f + 1 != tmpArray.size()) {
                            buffWriter.write("\n");
                        } else {
                            buffWriter.flush();
                            if (n + 1 != buildingList.size()) {
                                buffWriter.write("]},\n");
                            } else {
                                buffWriter.write("]}]}\n");
                            }
                        }
                    }
                    buffWriter.write("\n");
                    buffWriter.flush();
                }
            } else {
                BufferedWriter userBuffWriter = new BufferedWriter(new FileWriter("src/main/java/com/example/wesgeosys/accountData.json"));
                userBuffWriter.write("[");
                for (int n = 0; n < accounts.size(); n++) {
                    userBuffWriter.write(accounts.get(n).toString());
                    if (n + 1 != accounts.size()) {
                        userBuffWriter.write(",\n");
                    } else {
                        userBuffWriter.write("]");
                    }
                    userBuffWriter.flush();
                }
            }
        } catch (IOException e) {
            System.out.println("IOException");
            e.printStackTrace();
        }
    }
}
