package GUI.JavaFX.Scenes.WelcomeSplash;

import Design.Logger;
import GUI.JavaFX.Launcher;
import GUI.Singleton_manager.ClassManager;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.MenuBar;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.util.Duration;

import java.awt.*;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;

/**
 * @author Moose.
 */
public class WelcomeSplashController implements Initializable {

    @FXML
    private Text textWelcomeSplash;

    @FXML
    private VBox mainWelcome;

    @FXML
    private MenuBar menuBar;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        menuBar.getMenus().setAll(ClassManager.getMenuBar().getMenus());
        ToggleButton toggle = new ToggleButton("Toggle color");

        textWelcomeSplash.setText("Welcome to Quokka");

        mainWelcome.backgroundProperty().bind(Bindings.when(toggle.selectedProperty())
                .then(new Background(new BackgroundFill(Color.CORNFLOWERBLUE, CornerRadii.EMPTY, Insets.EMPTY)))
                .otherwise(new Background(new BackgroundFill(Color.LIGHTBLUE , CornerRadii.EMPTY, Insets.EMPTY))));

        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(2),event -> {
            Logger.notice("Reached Controller Splash -> +2 seconds");
            Logger.confirm("RUNNING MIRIO SWITCH -> LoginScreen");
            try {
                Launcher.setScreen(ClassManager.getLoginScreen().getScreen());
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }));

        timeline.play();
    }
}
