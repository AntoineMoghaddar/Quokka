package GUI.JavaFX.Scenes.WelcomeSplash;

import Design.Logger;
import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ToggleButton;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.VBox;
import javafx.scene.paint.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

import java.awt.*;
import javafx.geometry.Insets;
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

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        ToggleButton toggle = new ToggleButton("Toggle color");

        textWelcomeSplash.setText("Welcome to Quokka");

        mainWelcome.backgroundProperty().bind(Bindings.when(toggle.selectedProperty())
                .then(new Background(new BackgroundFill(Color.CORNFLOWERBLUE, CornerRadii.EMPTY, Insets.EMPTY)))
                .otherwise(new Background(new BackgroundFill(Color.LIGHTBLUE , CornerRadii.EMPTY, Insets.EMPTY))));

//        mainWelcome.setBackground(new Background(new BackgroundFill(new Color(215,209,209, 100))));
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                Logger.notice("Reached Controller Splash -> +2 seconds");
            }
        }, 2000);
    }
}
