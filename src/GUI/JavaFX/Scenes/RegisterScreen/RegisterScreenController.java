package GUI.JavaFX.Scenes.RegisterScreen;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by Brent Verharen on 11/04/2017.
 */
public class RegisterScreenController implements Initializable{
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