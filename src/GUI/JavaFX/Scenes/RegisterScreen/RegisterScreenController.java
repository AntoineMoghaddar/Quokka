package GUI.JavaFX.Scenes.RegisterScreen;

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
import sun.rmi.runtime.Log;

import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

/**
 * @author Moose
 */
public class RegisterScreenController implements Initializable{

    @FXML
    private VBox mainRegister;

    @FXML
    private MenuBar menuBar;

    @FXML
    private RadioButton maleButton, femaleButton, helicopterbutton;

    @FXML
    private TextField registerUsername;

    @FXML
    private PasswordField passwordRegister, confirmpasswordRegister;

    @FXML
    private CheckBox acceptRights;

    @FXML
    private Button confirmRegistrationButton;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Login_Process login_process = Login_Process.getInstance();
        menuBar.getMenus().setAll(ClassManager.getMenuBar().getMenus());
        ToggleButton toggle = new ToggleButton("Toggle color");

        mainRegister.backgroundProperty().bind(Bindings.when(toggle.selectedProperty())
                .then(new Background(new BackgroundFill(Color.CORNFLOWERBLUE, CornerRadii.EMPTY, Insets.EMPTY)))
                .otherwise(new Background(new BackgroundFill(Color.LIGHTBLUE, CornerRadii.EMPTY, Insets.EMPTY))));


        if (registerUsername.getText() != null
                && passwordRegister.getText() != null
                && (Objects.equals(passwordRegister.getText(), confirmpasswordRegister.getText()))
                && acceptRights.isSelected() && (maleButton.isSelected()
                || femaleButton.isSelected()
                || helicopterbutton.isSelected())) {

            confirmpasswordRegister.setOnAction(event -> {
//                login_process.Register(registerUsername.getText(), )
            });

        }

    }
}
