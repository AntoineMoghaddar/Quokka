package GUI.JavaFX.Scenes.MainScreen;

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
public class MainScreen implements Buildable {

    @Override
    public Parent getScreen() throws IOException {
        Logger.confirm("Entered MainScreen");
        return FXMLLoader.load(getClass().getResource("MainScreenScreen.fxml"));
    }
}
