package GUI.JavaFX;

/**
 * @author Moose.
 */

import Design.Logger;
import GUI.JavaFX.Scenes.MainScreen.MessageProcessing.*;
import GUI.JavaFX.Scenes.LoginScreen.LoginProcessing.*;
import GUI.Singleton_manager.ClassManager;
import GUI.io.MessageExporter;
import GUI.io.MessageImporter;
import GUI.io.UserImporter;
import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.awt.*;
import java.io.IOException;
import java.util.List;

public class Launcher extends Application {


    private static Stage current = null;
    private List<Message> messages;
    private List<User> users;


    /**
     * Set the current screen of the window
     * @param screen screen of the window
     */
    public static void setScreen(Parent screen){

        current.setScene(new Scene(screen, Toolkit.getDefaultToolkit().getScreenSize().getWidth(),
                Toolkit.getDefaultToolkit().getScreenSize().getHeight() - 100));

    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        ClassManager.load(this);

        try {
            messages = MessageImporter.importMessages();
            users = UserImporter.importUsers();

        } catch (Exception ex) {
            Logger.err(ex.getCause());
            Logger.err("Could not import Messages");
            ex.printStackTrace();
        }

        current = primaryStage;
        current.setHeight(750);
        current.setWidth(850);

        current.setOnCloseRequest(Launcher::close);
//        File file = new File("/img.png");
//        Logger.err(file.toPath() + " Value of File");
//        current.getIcons().add(new Image(getClass().getResourceAsStream(file.toString())));

        primaryStage.setTitle("Quokka Network");

        setScreen(ClassManager.getWelcomeSplash().getScreen());
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    public static void close(WindowEvent... event) {

        boolean directEvent = false;
        if (event.length != 0)
            directEvent = true;

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("");
        alert.setHeaderText("Are you sure you want to close the application?");
        alert.showAndWait();

        if (alert.getResult().equals(ButtonType.CANCEL)) {
            if (directEvent)
                event[0].consume();
            return;
        }


        //TODO This part does not work due to some errors, /However this part should not work since it is rewritten somewhere else
//        try {
//            MessageExporter.exportResults();
//        } catch (Exception ex) {
//            System.err.println("Could not export results");
//        }

        if (!directEvent)
            getScreen().close();
    }


    public List<Message> getMessages() {
        return messages;
    }

    private static Stage getScreen() {
        if (current == null) Logger.err("Returning null, pls don't");
        return current;
    }

    public static void main(String[] args) {
        launch(args);
    }

}
