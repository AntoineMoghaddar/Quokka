package GUI.io;

import GUI.Singleton_manager.ClassManager;

import GUI.JavaFX.Scenes.MainScreen.MessageProcessing.*;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;


/**
 * @author Moose
 * @Definition Exports messages at the end of every session with the system
 * @use writes all data into the correct file
 */
public class MessageExporter {

    public static void exportResults() throws IOException {

        List<Message> results = ClassManager.getLauncher().getMessages();

        PrintWriter printWriter = new PrintWriter(new File("messages.txt"));

        for (Message r : results) {

            String s = r.getMessage() + ";" + r.getSender();

            printWriter.write(s + "\n");
        }

        printWriter.flush();
        printWriter.close();

        System.out.println("Exported " + results.size() + " messages");
    }
}
