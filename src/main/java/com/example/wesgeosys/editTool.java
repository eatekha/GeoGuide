package com.example.wesgeosys;

import org.json.simple.JSONObject;
import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;


public class editTool {


    protected String username;

    protected boolean adminPerms;

    public JSONArray buildingData;

    protected JSONArray accountData;

    final String finalAdminName = "Admin";

    enum layerType {
        Class,
        Lab,
        CollaborationSpace,
        Washroom,
        Elevator,
        Navigation,
        Default,
    }

    public editTool(JSONArray buildings, String username, Boolean admin) {
        JSONParser jsonParser = new JSONParser();
        try (FileReader reader = new FileReader("src/main/java/com/example/wesgeosys/accountData.json")) {
            this.accountData = (JSONArray) jsonParser.parse(reader);
        } catch (IOException e) {
            System.out.println("IOException");
        } catch (ParseException e) {
            System.out.println("ParseException");
        }
        this.adminPerms = admin;
        this.buildingData = buildings;
        this.username = username;
    }

    public void persistData() {
        try {
            if (adminPerms) {
                BufferedWriter buffWriter = new BufferedWriter(new FileWriter("src/main/java/com/example/wesgeosys/defaultPOI.json"));
                JSONObject tmpObj = null;
                JSONArray tmpArray = new JSONArray();
                buffWriter.write("{\"buildings\":[");
                for (int n = 0; n < buildingData.size(); n++) {
                    tmpObj = (JSONObject) buildingData.get(n);
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
                            if (n + 1 != buildingData.size()) {
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
                for (int n = 0; n < accountData.size(); n++) {
                    userBuffWriter.write(accountData.get(n).toString());
                    if (n + 1 != accountData.size()) {
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


    public void createFloor(JSONObject currentBuilding, String imageName) {
        if (adminPerms) {
            JSONObject newFloor = new JSONObject();
            JSONArray poiList = createPOI();
            newFloor.put("imageFileName", imageName);
            newFloor.put("pointsOfInterest", poiList);
            JSONArray tmpArray = (JSONArray) currentBuilding.get("floors");
            tmpArray.add(tmpArray.size(), newFloor);
            persistData();
        } else {
            System.out.println("INVALID PERMISSIONS TO CREATE FLOOR");
        }
    }


    public void deleteFloor(JSONArray currentBuild, int floorNum) {
        if (adminPerms) {
            currentBuild.remove(floorNum);
            persistData();
        } else {
            System.out.println("INVALID PERMISSIONS TO DELETE FLOOR");
        }
    }

    public void createPOI(JSONArray poiList) {
        JSONObject defaultPOI = new JSONObject();
        defaultPOI.put("layerType", "Default");
        defaultPOI.put("visibility", true);
        defaultPOI.put("favourite", false);
        defaultPOI.put("description", "DefaultDescription");
        defaultPOI.put("name", "Default POI Name");
        defaultPOI.put("xCord", 0);
        defaultPOI.put("yCord", 0);
        defaultPOI.put("roomNum", "DefaultRoomNum");
        if (adminPerms) {
            defaultPOI.put("builtInPOI", true);
        } else {
            defaultPOI.put("builtInPOI", false);
        }
        poiList.add(0, defaultPOI);
        persistData();
    }


    public void modifyPOI(JSONObject poi, String newName, String newDescription, int newX, int newY, String newRoomNum, String newLayerType) {
        if (poi.get("builtInPOI").equals(true)) {
            if (adminPerms) {
                poi.replace("name", newName);
                poi.replace("description", newDescription);
                poi.replace("xCord", newX);
                poi.replace("yCord", newY);
                poi.replace("roomNum", newRoomNum);
                poi.replace("layerType", newLayerType);
                persistData();
            } else {
                System.out.println("INVALID PERMISSIONS");
            }
        } else {
            JSONObject userObj;
            for (int n = 0; n < accountData.size(); n++) {
                userObj = (JSONObject) accountData.get(n);
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
                            persistData();
                        }
                    }
                }
            }
        }
    }


    public void deletePOI(JSONArray poiList, JSONObject currentPOI) {
        JSONObject tmpObj;
        for (int n = 0; n < poiList.size(); n++) {
            tmpObj = (JSONObject) poiList.get(n);
            if (tmpObj.get("name").equals(currentPOI.get("name")) && tmpObj.get("roomNum").equals(currentPOI.get("roomNum"))) {
                poiList.remove(n);
                persistData();
            }
        }
    }


    public void addUserPOI(String buildName, int floorNum) {
        JSONObject userObj;
        for (int n = 0; n < accountData.size(); n++) {
            userObj = (JSONObject) accountData.get(n);
            if (userObj.get("username")==null){}
            else{
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
                persistData();
            }
        }}
    }

    public JSONArray createFloor() {
        JSONArray floors = new JSONArray();
        JSONArray poiList = createPOI();
        JSONObject defaultFloorObj = new JSONObject();
        defaultFloorObj.put("imageFileName", "DefaultFileName");
        defaultFloorObj.put("pointsOfInterest", poiList);
        floors.add(0, defaultFloorObj);
        return floors;
    }


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
        if (adminPerms) {
            defaultPOI.put("builtInPOI", true);
        } else {
            defaultPOI.put("builtInPOI", false);
        }
        poiList.add(0, defaultPOI);
        return poiList;
    }

    public int findIndex(String buildName) {
        for (int i = 0; i < buildingData.size(); i++) {
            JSONObject buildings = (JSONObject) buildingData.get(i);
            String buildingname = (String) buildings.get("Building");
            if (buildName.equals(buildingname)) {
                return i;
            }
        }
        System.out.println("BUILDING DOES NOT EXIST.");
        return -1;
    }

    public void createBuilding(String buildName) {
        if (adminPerms) {
            JSONObject newBuild = new JSONObject();
            JSONArray newFloor = createFloor();
            newBuild.put("Building", buildName);
            newBuild.put("floors", newFloor);
            buildingData.add(newBuild);
            persistData();
        } else {
            System.out.println("INVALID PERMISSIONS TO CREATE BUILDING");
        }
    }

    public void modifyBuilding(JSONObject currentBuilding, String newBuildName) {
        if (adminPerms) {
            int num = findIndex(currentBuilding.get("Building").toString());
            buildingData.remove(num);
            currentBuilding.replace("Building", newBuildName);
            buildingData.add(currentBuilding);
            persistData();
        } else {
            System.out.println("INVALID PERMISSIONS TO EDIT BUILDING");
        }
    }


    public void deleteBuilding(String buildName) {
        if (adminPerms) {
            buildingData.remove(buildingData.get(findIndex(buildName)));
            persistData();
        } else {
            System.out.println("INVALID PERMISSIONS TO DELETE BUILDING");
        }
    }


    public void toggleFavourite(String currentBuild, int floorNum, JSONObject currPOI, Boolean favTF) {
        JSONObject userObj;
        for (int n = 0; n < accountData.size(); n++) {
            userObj = (JSONObject) accountData.get(n);
            if (userObj.get("username")==null){}
            else{
            String string = userObj.get("username").toString();
            if (string.equals(username)) {
                JSONArray poiList = (JSONArray) userObj.get("favourites");
                poiList.remove(currPOI);
                currPOI.put("building", currentBuild);
                currPOI.put("floorNum", floorNum);
                currPOI.replace("favourite", favTF);
                poiList.add(currPOI);
                persistData();
            }
        }}
    }


    public void deleteUserPOI(JSONObject poi) {
        JSONObject userObj;
        for (int n = 0; n < accountData.size(); n++) {
            userObj = (JSONObject) accountData.get(n);
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
                persistData();
            }
        }
    }

}
