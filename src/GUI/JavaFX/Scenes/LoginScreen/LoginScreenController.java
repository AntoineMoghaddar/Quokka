package GUI.JavaFX.Scenes.LoginScreen;

import Design.Logger;
import GUI.Singleton_manager.ClassManager;
import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.MenuBar;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by Brent Verharen on 11/04/2017.
 */
public class LoginScreenController implements Initializable {

    @FXML
    private VBox loginBox;
    @FXML
    private MenuBar menu;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        ToggleButton toggle = new ToggleButton("Toggle color");

        menu.getMenus().setAll(ClassManager.getMenuBar().getMenus());

        loginBox.backgroundProperty().bind(Bindings.when(toggle.selectedProperty())
                .then(new Background(new BackgroundFill(Color.CORNFLOWERBLUE, CornerRadii.EMPTY, Insets.EMPTY)))
                .otherwise(new Background(new BackgroundFill(Color.LIGHTBLUE, CornerRadii.EMPTY, Insets.EMPTY))));



        Logger.debug("reached login controller");
    }
}
