package GUI.JavaFX;

import javafx.scene.Parent;

import java.io.IOException;

/**
 * @author Moose.
 */
public interface Buildable {

    Parent getScreen() throws IOException;

}
