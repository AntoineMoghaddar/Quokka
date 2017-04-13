package GUI.Singleton_manager;

import Design.Logger;
import GUI.JavaFX.Launcher;
import GUI.JavaFX.Scenes.LoginScreen.LoginProcessing.Login_Process;
import GUI.JavaFX.Scenes.LoginScreen.LoginScreen;
import GUI.JavaFX.Scenes.MainScreen.MainScreen;
import GUI.JavaFX.Scenes.RegisterScreen.RegisterScreen;
import GUI.JavaFX.Scenes.WelcomeSplash.WelcomeSplash;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;

import java.io.IOException;

/**
 * ClassPreLoader to get all the classes in one central place to call them from everywhere
 */
public class ClassManager {

    private static Launcher launcher;
    private static WelcomeSplash welcomeSplash;
    private static LoginScreen loginScreen;
    private static MainScreen mainScreen;
    private static Login_Process login_process;

    private static RegisterScreen registerScreen;
    private static MenuBar menuBar;


    public static void load(Launcher _launcher){
        login_process = Login_Process.getInstance();
        launcher = _launcher;

        menuBar = new MenuBar();
        menuBar.getMenus().addAll(getDefaultMenuItems());
        menuBar.setUseSystemMenuBar(true);

        welcomeSplash = new WelcomeSplash();
        loginScreen = new LoginScreen();
        mainScreen = new MainScreen();
        registerScreen = new RegisterScreen();
    }

    private static Menu[] getDefaultMenuItems(){

        Menu navigation = new Menu("Navigation");
        MenuItem mainMenu = new MenuItem("Main Menu");
        MenuItem stroke = new SeparatorMenuItem();
        MenuItem stroke2 = new SeparatorMenuItem();
        MenuItem logout = new MenuItem("Logout");
        MenuItem exit = new MenuItem("Exit");


        mainMenu.setOnAction(e -> {
            try {
                Launcher.setScreen(getWelcomeSplash().getScreen());
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });

        exit.setOnAction(e -> Launcher.close());
        logout.setOnAction(event -> {
            login_process.setCurrentUser();
            try {
                Launcher.setScreen(ClassManager.getWelcomeSplash().getScreen());
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        navigation.getItems().addAll(mainMenu, stroke, logout, stroke2, exit);

        Menu help = new Menu("Help");
        MenuItem about = new MenuItem("About Quokka");
        about.setOnAction(e -> {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("");
            alert.setContentText("Quokka is a online communication system\n" +
                    "made by a team of four developers:\n" +
                    "Antoine Moghaddar\n" +
                    "Rowin Veneman\n" +
                    "Rick Röttjers\n" +
                    "Brent Verharen\n");

            alert.show();
        });
        help.getItems().add(about);

        Menu admin = new Menu("Admin");
        CustomMenuItem login = new CustomMenuItem();

        VBox vbox = new VBox();
        TextField name = new TextField();
        PasswordField pass = new PasswordField();
        Button loginButton = new Button("Log in");

        name.setPromptText("Name");
        pass.setPromptText("Password");

        name.setPrefWidth(150);
        pass.setPrefWidth(150);
        loginButton.setPrefWidth(150);

        loginButton.setOnAction(e -> {
            Logger.notice(name.getText().equals("admin") && pass.getText().equals("admin") ?
                    "Entered Admin Panel" : "USER NOT RECOGNIZED AS ADMIN");
        });

        vbox.setAlignment(Pos.CENTER);
        vbox.getChildren().addAll(name, pass,loginButton);
        login.setContent(vbox);

        admin.getItems().add(login);

        return new Menu[]{navigation, help, admin};
    }

    public static MenuBar getMenuBar() {
        return menuBar;
    }

    public static Launcher getLauncher() {
        return launcher;
    }

    public static WelcomeSplash getWelcomeSplash() {
        return welcomeSplash;
    }

    public static LoginScreen getLoginScreen() {
        return loginScreen;
    }

    public static MainScreen getMainScreen() {
        return mainScreen;
    }

    public static RegisterScreen getRegisterScreen() {
        return registerScreen;
    }
}

