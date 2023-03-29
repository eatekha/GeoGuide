package com.example.wgis;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.Objects;

public class loginController {
    @FXML
    private ImageView myImage;


    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    private Stage loginStage;

    public void setLoginStage(Stage loginStage) throws FileNotFoundException {
      myImage.setImage(new Image(new FileInputStream("src/main/java/LoginImage/Campus.jpg")));
        this.loginStage = loginStage;
    }

    @FXML
    void handleLoginButtonAction(ActionEvent event) {
        String username = usernameField.getText();
        String password = passwordField.getText();

        if (!isValidUser(username, password)) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Invalid User Type");
            alert.setHeaderText("Invalid user type");
            alert.setContentText("Please enter valid credentials and try again.");
            alert.showAndWait();
        }

        if (isValidUser(username, password)) {
            try {
                if (username.equals("user") && password.equals("userpassword")) {
                    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("mainSystemPage.fxml"));
                    Parent root = fxmlLoader.load();
                    Stage stage = new Stage();
                    stage.setScene(new Scene(root));
                    stage.show();
                    loginStage.close();
                } else if (username.equals("admin") && password.equals("adminpassword")) {
                    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("adminSystemPage.fxml"));
                    Parent root = fxmlLoader.load();
                    Stage stage = new Stage();
                    stage.setScene(new Scene(root));
                    stage.show();
                    loginStage.close();
                } else {
                    throw new Exception("Invalid user type");
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (Exception e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Invalid User Type");
                alert.setHeaderText("Invalid user type");
                alert.setContentText("Please enter valid credentials and try again.");
                alert.showAndWait();
            }
        }
    }

    private boolean isValidUser(String username, String password) {
        try {
            File file = new File("src/main/java/com/example/wgis/accountsSet.xml");

            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(file);

            doc.getDocumentElement().normalize();

            NodeList nList = doc.getElementsByTagName("user");

            for (int temp = 0; temp < nList.getLength(); temp++) {
                Node nNode = nList.item(temp);

                if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element eElement = (Element) nNode;

                    String storedUsername = eElement.getElementsByTagName("username").item(0).getTextContent();
                    String storedPassword = eElement.getElementsByTagName("password").item(0).getTextContent();

                    if (username.equals(storedUsername) && password.equals(storedPassword)) {
                        return true;
                    }
                }
            }
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
