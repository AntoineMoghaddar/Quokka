package GUI.JavaFX.Scenes.RegisterScreen;

import Design.Logger;
import GUI.JavaFX.Buildable;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

import java.io.IOException;

/**
 * @author Moose.
 */
public class RegisterScreen implements Buildable {
    @Override
    public Parent getScreen() throws IOException {
        Logger.notice("reached RegisterScreen Build");
//        Logger.confirm(FXMLLoader.load(getClass().getResource("RegisterScreenScreen.fxml")));
        return
                FXMLLoader
                        .load(
                                getClass()
                                        .getResource(
                                                "RegisterScreenScreen.fxml"
                                        ));
    }
}
