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
        return FXMLLoader.load(getClass().getResource("WelcomeSplashScreen.fxml"));
    }
}
