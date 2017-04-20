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

    private Message_Process() {
        messages = new ArrayList<>();
        messagesReWrite = new ArrayList<>();
        readMessagesFile("messages.txt");
    }

    public static Message_Process getInstance() {
        if (instance == null) {
            instance = new Message_Process();
        }
        return instance;
    }

    public int readMessagesFile(String filename) {
        Logger.notice("Applied for reading in messages list");
        int amountObjects = 0;
        try {
            Scanner fileScanner = new Scanner(new File(filename));
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
        return amountObjects;
    }

    public Message processLineUserFile(String line) throws LineException {
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
    }//done

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


    public String toString() {
        return "Message_Process{" +
                "messages=" + messages +
                '}';
    }

    public  ArrayList<Message> getMessages() {
        Logger.console(toString());
        return messages;
    }

    public void addMessages(Message message){
        messages.add(message);
    }
}
