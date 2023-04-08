import com.example.wesgeosys.accountClass;
import com.example.wesgeosys.logController;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.json.simple.parser.ParseException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

public class logControllerTest {

    private logController controller;

    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("loginGUI.fxml"));
        Scene scene = new Scene(loader.load(), 322.0, 391.0);
        stage.setScene(scene);
        stage.show();
        controller = loader.getController();
    }

    @BeforeEach
    public void setUp() throws IOException, ParseException {
        controller.accountNum = new accountClass("src/test/resources/accountData.json");
    }

    @Test
    public void testSubmitSelectionWithValidCredentials() {
    }

    @Test
    public void testSubmitSelectionWithInvalidCredentials() {

    }

    @Test
    public void testSignupSelection() {

    }
}
