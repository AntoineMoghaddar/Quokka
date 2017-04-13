package GUI.JavaFX.Scenes.MainScreen;

import Design.Logger;
import GUI.JavaFX.Buildable;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

import java.io.IOException;

/**
 * @author Moose.
 */
public class MainScreen implements Buildable {

    @Override
    public Parent getScreen() throws IOException {
        Logger.confirm("Entered MainScreen");
        return FXMLLoader.load(getClass().getResource("MainScreenScreen.fxml"));
    }
}
