package GUI.JavaFX.Scenes.LoginScreen;

import Design.Logger;
import GUI.JavaFX.Buildable;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

import java.io.IOException;

/**
 * @author Moose.
 */
public class LoginScreen implements Buildable {

    @Override
    public Parent getScreen() throws IOException {
        Logger.console("reached LoginScreen Build");
        return FXMLLoader.load(getClass().getResource("LoginScreenScreen.fxml"));
    }
}
