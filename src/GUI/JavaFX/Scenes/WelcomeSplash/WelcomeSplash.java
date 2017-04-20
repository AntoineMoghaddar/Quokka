package GUI.JavaFX.Scenes.WelcomeSplash;

import Design.Logger;
import GUI.JavaFX.Buildable;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

import java.io.IOException;

/**
 * @author Moose.
 * @use FXML Loader
 * @Definition Loads in the right FXML file and processes every connection between the controller and the FXML file
 */
public class WelcomeSplash implements Buildable {
    @Override
    public Parent getScreen() throws IOException {
        Logger.notice("reached WelcomeSplash Build");
        return FXMLLoader.load(getClass().getResource("WelcomeSplashScreen.fxml"));
    }
}
