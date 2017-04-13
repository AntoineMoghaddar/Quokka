package GUI.JavaFX.Scenes.LoginScreen;

import Design.Logger;
import GUI.JavaFX.Launcher;
import GUI.JavaFX.Scenes.LoginScreen.LoginProcessing.Login_Process;
import GUI.Singleton_manager.ClassManager;
import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * @author Moose && Brent Verharen
 */
public class LoginScreenController implements Initializable {

    @FXML
    private VBox loginBox;
    @FXML
    private MenuBar menu;

    @FXML
    private Button loginButton, registerButton;

    @FXML
    private PasswordField tfPassword;

    @FXML
    private TextField tfUserName;

    @FXML
    Label loginconfirmation;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Login_Process login_process = Login_Process.getInstance();
        ToggleButton toggle = new ToggleButton("Toggle color");
            menu.getMenus().setAll(ClassManager.getMenuBar().getMenus());

        loginBox.backgroundProperty().bind(Bindings.when(toggle.selectedProperty())
                .then(new Background(new BackgroundFill(Color.CORNFLOWERBLUE, CornerRadii.EMPTY, Insets.EMPTY)))
                .otherwise(new Background(new BackgroundFill(Color.LIGHTBLUE, CornerRadii.EMPTY, Insets.EMPTY))));

        loginButton.setOnAction(event -> {
            if (login_process.login(tfUserName.getText(), tfPassword.getText())) {
                Logger.confirm("Login granted");
                loginconfirmation.setText("LOGIN HAS NOT BEEN APPROVED");
                loginconfirmation.setTextFill(Color.GREEN);
                try {
                    Launcher.setScreen(ClassManager.getMainScreen().getScreen());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                Logger.err("Access Denied");
                loginconfirmation.setText("LOGIN HAS NOT BEEN APPROVED");
                loginconfirmation.setTextFill(Color.RED);
            }
        });

        registerButton.setOnAction(event -> {
            try {
                Launcher.setScreen(ClassManager.getRegisterScreen().getScreen());
            } catch (IOException e) {
                e.printStackTrace();
            }

        });

        Logger.debug("reached login controller");
    }
}
