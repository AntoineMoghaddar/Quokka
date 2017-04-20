package GUI.io;

import GUI.JavaFX.Scenes.MainScreen.MessageProcessing.*;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class MessageImporter {

    public static List<Message> importMessages() throws Exception {

        Scanner scanner = new Scanner(new File("messages.txt"));
        List<Message> results = new ArrayList<>();

        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();

            String[] parts = line.split(";");

            parts[2] = parts[2].replaceAll("±", "\n");


            //TODO Modify this so that it compiles the current settings of messages
//            results.add(new Message(parts[0], parts[2]));
        }

        System.out.println("Imported " + results.size() + " results");
        return results;
    }
}
