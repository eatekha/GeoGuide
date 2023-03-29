module com.example.wgis {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.xml;
    requires java.net.http;
    requires json.simple;


    opens com.example.wgis to javafx.fxml;
    exports com.example.wgis;
}