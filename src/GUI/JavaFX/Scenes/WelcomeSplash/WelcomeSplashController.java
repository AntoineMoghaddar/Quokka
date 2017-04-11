package GUI.JavaFX.Scenes.WelcomeSplash;

import Design.Logger;
import javafx.fxml.Initializable;


import java.net.URL;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;

/**
 * @author Moose.
 */
public class WelcomeSplashController implements Initializable {

    @Override
    public void initialize(URL location, ResourceBundle resources) {
//        menuBar.getMenus().setAll(ClassManager.getMenuBar().getMenus());

        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                Logger.notice("Reached Controller Splash -> +2 seconds");
            }
        }, 2000);
    }
}
