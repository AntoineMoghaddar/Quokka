package GUI.JavaFX.Scenes.LoginScreen;

import Design.Logger;
import GUI.Singleton_manager.ClassManager;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Brent Verharen on 11/04/2017.
 */
public class LoginScreenController implements Initializable{
    @FXML
    private MenuBar menu;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        menu.getMenus().setAll(ClassManager.getMenuBar().getMenus());

        Logger.debug("reached login controller");
    }
}
