package GUI.JavaFX.Scenes.MainScreen;

import GUI.Singleton_manager.ClassManager;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.MenuBar;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * @author Moose.
 */
public class MainScreenController implements Initializable {

    @FXML
    private MenuBar menuBar;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        menuBar.getMenus().setAll(ClassManager.getMenuBar().getMenus());

    }
}
