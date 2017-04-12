package GUI.Singleton_manager;

import GUI.JavaFX.Launcher;
import GUI.JavaFX.Scenes.LoginScreen.LoginScreen;
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
//    private static CalculationScreen calculationScreen;
//    private static OpenScreen openScreen;
//    private static AdminScreen adminScreen;
//    private static StepScreen stepScreen;
//    private static SaveBox saveBox;

    private static MenuBar menuBar;
//
//    private static ChainAPL chainAPL;
//    private static ChainController chainController;

    public static void load(Launcher _launcher){

        launcher = _launcher;

        menuBar = new MenuBar();
        menuBar.getMenus().addAll(getDefaultMenuItems());
        menuBar.setUseSystemMenuBar(true);

        welcomeSplash = new WelcomeSplash();
        loginScreen = new LoginScreen();
//
//        chainAPL = new ChainAPL();
//        chainController = new ChainController();
    }

    private static Menu[] getDefaultMenuItems(){

        Menu navigation = new Menu("Navigatie");
        MenuItem mainMenu = new MenuItem("Hoofdmenu");
//        MenuItem calculate = new MenuItem("Nieuwe berekening");
        MenuItem stroke = new SeparatorMenuItem();
        MenuItem exit = new MenuItem("Verlaat programma");

        mainMenu.setOnAction(e -> {
            try {
                Launcher.setScreen(getWelcomeSplash().getScreen());
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });

//        calculate.setOnAction(e -> {
//            try {
//                Launcher.setScreen(getCalculationScreen().getScreen());
//            } catch (IOException ex) {
//                ex.printStackTrace();
//            }
//        });

//        open.setOnAction(e -> {
//            try {
//                Launcher.setScreen(getOpenScreen().getScreen());
//            } catch (IOException ex) {
//                ex.printStackTrace();
//            }
//        });

//        exit.setOnAction(e -> Launcher.close());

        navigation.getItems().addAll(mainMenu, stroke, exit);

        Menu help = new Menu("Help");
        MenuItem about = new MenuItem("Over Ketting Calculator");
        about.setOnAction(e -> {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("");
            alert.setHeaderText("About");
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

        name.setPromptText("Naam");
        pass.setPromptText("Wachtwoord");

        name.setPrefWidth(150);
        pass.setPrefWidth(150);
        loginButton.setPrefWidth(150);

        loginButton.setOnAction(e -> {
            if (name.getText().equals("admin") && pass.getText().equals("admin"))
                System.out.println("Enter Admin Screen");
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
}
