package GUI.JavaFX.Scenes.WelcomeSplash;

import Design.Logger;
import GUI.JavaFX.Buildable;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

import java.io.IOException;

/**
 * @author Moose.
 */
public class WelcomeSplash implements Buildable {
    @Override
    public Parent getScreen() throws IOException {
        Logger.notice("reached WelcomeSplash Build");
//        Logger.confirm(FXMLLoader.load(getClass().getResource("WelcomeSplashScreen.fxml")));
        return
                FXMLLoader
                        .load(
                                getClass()
                                        .getResource(
                                                "WelcomeSplashScreen.fxml"
                                        ));
    }
}
