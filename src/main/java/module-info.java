module com.example.wgis {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.xml;
    requires java.net.http;
//<<<<<<< Updated upstream
    requires json.simple;
//=======
//    requires org.json;
//>>>>>>> Stashed changes


    opens com.example.wgis to javafx.fxml;
    exports com.example.wgis;
}