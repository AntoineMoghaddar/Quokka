package GUI.JavaFX.Scenes.WelcomeSplash;

import Design.Logger;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;

/**
 * @author Moose.
 */
public class WelcomeSplashController implements Initializable {

    @FXML
    private ImageView iv;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        File file = new File("/img.png");
        Image img = new Image(file.toURI().toString());
        iv.setImage(img);
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                Logger.notice("Reached Controller Splash -> +2 seconds");
            }
        }, 2000);
    }
}
