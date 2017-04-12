package GUI.JavaFX.Scenes.LoginScreen;

import Design.Logger;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Brent Verharen on 11/04/2017.
 */
public class LoginScreenController implements Initializable{
    @FXML
    private Button button;
    private TextField username;
    private PasswordField password;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Button button = new Button();
        TextField username = new TextField();
        PasswordField password = new PasswordField();
    }
}
