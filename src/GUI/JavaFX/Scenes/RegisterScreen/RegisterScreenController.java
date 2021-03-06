package GUI.JavaFX.Scenes.RegisterScreen;

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
import java.util.Objects;
import java.util.ResourceBundle;

/**
 * @author Moose
 * @Definition Controller for this Screen
 * @use Sets all values and gives functionality to the items in the view
 */
public class RegisterScreenController implements Initializable{

    @FXML
    private VBox mainRegister;

    @FXML
    private MenuBar menuBar;

    @FXML
    private RadioButton maleButton, femaleButton, helicopterbutton;

    @FXML
    private TextField registerUsername, registerEmail;

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


        confirmRegistrationButton.setOnAction(event -> {
            Logger.debug("reached register if statement");
            if (registerUsername.getText() != null
                    && passwordRegister.getText() != null
                    && registerEmail.getText() != null
                    && (Objects.equals(passwordRegister.getText(), confirmpasswordRegister.getText()))
                    && acceptRights.isSelected() && (maleButton.isSelected()
                    || femaleButton.isSelected()
                    || helicopterbutton.isSelected())) {

                String secretKey = ("YES" + Math.random() * 10000 + 1);
                login_process.Register(
                        registerUsername.getText(),
                        login_process.SHA512_encoder(passwordRegister.getText(), secretKey),
                        registerEmail.getText(),
                        (maleButton.isSelected()) ? maleButton.getText() :
                                ((femaleButton.isSelected()) ? femaleButton.getText() : helicopterbutton.getText()),
                        secretKey
                );

//                new UpdateManager().execute();

                try {
                    Launcher.setScreen(ClassManager.getMainScreen().getScreen());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                Logger.err("system was not able to register data");
            }
        });
    }
}
