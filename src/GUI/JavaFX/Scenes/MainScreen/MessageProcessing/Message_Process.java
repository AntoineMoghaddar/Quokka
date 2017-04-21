package GUI.JavaFX.Scenes.MainScreen.MessageProcessing;

import Design.Logger;
import GUI.JavaFX.Scenes.LoginScreen.LoginProcessing.Login_Process;
import Helperclasses.Exceptions.LineException;

import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * @author Moose.
 */
public class Message_Process {

    private ArrayList<Message> messages;
    private ArrayList<Message> messagesReWrite;
    private Login_Process login_process = Login_Process.getInstance();
    private static Message_Process instance;

    /**
     * @Definition Constructor of this class
     * @use Used in a singleton method
     * @-> Only one instance of this method can be used through the entire project.
     */
    private Message_Process() {
        messages = new ArrayList<>();
        messagesReWrite = new ArrayList<>();
        readMessagesFile();
    }


    /**
     * @return new instance or already existing instance of this class
     * @Definition initializer for setting up the instance used in this project
     * @use Singleton method initializer
     */
    public static Message_Process getInstance() {
        if (instance == null) {
            instance = new Message_Process();
        }
        return instance;
    }

    /**
     * @Definition File reader for reading in the messages list
     * @use a single file gets read in in order to load in all the messages
     * @improvements this could be done more general
     * @-> by making it more general you could give a file name as a parameter and read it in as well
     * @-> Making it more general would have made our program more clean and would prevent redundant code.
     * @-> This amount of objects refers to the amount of system users.
     */
    public void readMessagesFile() {
        Logger.notice("Applied for reading in messages list");
        int amountObjects = 0;
        try {
            Scanner fileScanner = new Scanner(new File("messages.txt"));
            int linenumber = 0;
            while (fileScanner.hasNextLine()) {
                linenumber++;
                String line = fileScanner.nextLine();
                if (!(line.startsWith("#")) && (!(line.isEmpty()))) {
                    try {
                        if (messagesReWrite.size() != messages.size()) {
//                            Logger.confirm(messagesReWrite.size() + "SIZE OF MESSAGE REWRITE; " +
//                                    + messages.size() + "SIZE OF MESSAGES");
                            messages.add(processLineUserFile(line));
                            amountObjects++;
                        } else {
                            messagesReWrite.add(processLineUserFile(line));
                        }
                    } catch (LineException le) {
                        Logger.err(le.getMessage() + " on line " + linenumber);
                    }
                }
            }
            Logger.confirm("Read in " + amountObjects + " messages");
        } catch (FileNotFoundException fnf) {
            fnf.printStackTrace();
        }
    }

    /**
     * @param line; the given line by @readMessagesFile
     * @return returns a new created user
     * @throws LineException; thrown if a line gives an error because it is non-existent etc
     * @Definition line processor of the filereader
     * @use this method processes the formatting of the given file and converts every line into an object.
     * @-> this new user is being add to a list in @readMessagesFile
     */
    private Message processLineUserFile(String line) throws LineException {
        Message newMessage;
        String[] lineData = line.split(";");
        if (line.isEmpty()) {
            throw new LineException("No data found in line");
        }

        if (lineData.length >= 2) {
            newMessage = new Message(
                    login_process.getUser(
                            lineData[0]),
                    login_process.getUser(
                            lineData[1]),
                    lineData[2]);
        } else {
            throw new LineException("Fill in all the blanks");
        }
        Logger.log("User " + newMessage.toString() + " processed");
        return newMessage;
    }

    /**
     * @param sender;   The Name of the current user who's logged in
     * @param receiver; The user who should receive the message
     * @param message;  The actual message the user wants to send
     * @Definition A file is located and will be written in.
     * @use the method retrieves the file location and writes the data in the given format in the file
     * @-> The secret key is automatically generated and will be used to XOR with the given "password" by using SHA512
     */
    public void fileWriter(String sender, String receiver, String message) {
        PrintWriter printWriter;
        Logger.notice("Applied for writing in messages.txt");
        try {
            printWriter = new PrintWriter(new FileWriter("messages.txt", true));
            printWriter.write(sender + ";" + receiver + ";" + message + ";\n");
            printWriter.flush();
            printWriter.close();

        } catch (IOException file) {
            Logger.err(file.getMessage());
        }
    }

    /**
     * @return String value of this current object
     * @Definition ToString method
     * @use convert into String value
     */
    public String toString() {
        return "Message_Process{" +
                "messages=" + messages +
                '}';
    }

    /**
     * @return The entire arraylist consistent of all messages
     * @Definition gives a complete arraylist with all the registered messages
     */
    public  ArrayList<Message> getMessages() {
        Logger.console(toString());
        return messages;
    }

    public void addMessages(Message message){
        messages.add(message);
    }
}
