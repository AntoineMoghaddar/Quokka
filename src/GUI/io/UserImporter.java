package GUI.io;

import Design.Logger;

import GUI.JavaFX.Scenes.LoginScreen.LoginProcessing.User;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class UserImporter {

    public static List<User> importUsers() throws Exception {

        Scanner scanner = new Scanner(new File("userlist.txt"));
        List<User> results = new ArrayList<>();

        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            String[] parts = line.split(";");
            parts[2] = parts[2].replaceAll("±", "\n");

            Logger.debug("New user has been imported: " + new User(parts[0], parts[2]).toString());
            results.add(new User(parts[0], parts[2]));
        }

        System.out.println("Imported " + results.size() + " results");
        return results;
    }
}
