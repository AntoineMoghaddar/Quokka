package Design;

import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * @author Moose (Antoine Moghaddar 05/10/2016)
 * This class is meant to process a login request/ register request but also able to read and write data in a textfile, these methods can be used in, for example, JavaFx projects
 */
public class Login_Process {
    private ArrayList<User> users = new ArrayList<>();
    private User currentUser;


    public int readUsersFile(String filename) {
        int amountObjects = 0;
        try {
            Scanner fileScanner = new Scanner(new File(filename));
            int linenumber = 0;
            while (fileScanner.hasNextLine()) {
                linenumber++;
                String line = fileScanner.nextLine();
                if (!(line.startsWith("#")) && (!(line.isEmpty()))) {
                    try {
                        users.add(processLineUserFile(line));
                        amountObjects++;
                    } catch (LineException le) {
                        System.out.println(le.getMessage() + " on line " + linenumber);
                    }
                }
            }
            Logger.confirm("Read in " + amountObjects + " objects");
        } catch (FileNotFoundException fnf) {
            System.out.println();
        }
        return amountObjects;
    }

    public User processLineUserFile(String line) throws LineException {
        User newUser;
        String[] lineData = line.split(";");
        if (line.isEmpty()) {
            throw new LineException("No data found in line");
        }

        if (lineData.length >= 2) {

            newUser = new User(lineData[0], lineData[1]);
        } else {
            throw new LineException("Fill in all the blanks");
        }

        return newUser;
    }//done

    public void fileWriter(String username, String password, String firstName, String lastName, String email) {
        PrintWriter printWriter;
        Logger.notice("Applied for writing in userfile");
        try {
            printWriter = new PrintWriter(new FileWriter("users.txt", true));
            printWriter.write(username + ";" + password + ";" + firstName +  ";" + lastName +  ";" + email + "\n");
            printWriter.flush();
            printWriter.close();

        } catch (IOException file) {
            Logger.err(file.getMessage());
        }
    }

    public boolean Register(String firstname, String lastName, String username, String email, String password) {
        boolean notaccessed;

        User newUser = new User(firstname, lastName, username, email, password);

        if (!checkValidRegister(newUser.getUsername())) {
            users.add(newUser);
            fileWriter(username, password, firstname, lastName, email);
            notaccessed = false;
        } else {
            Logger.err("Try again");
            notaccessed = true;
        }
        return notaccessed;
    }

    public boolean login(String username, String password) {
        Logger.notice("User login request");

        boolean access = false;

        if (users.isEmpty()) {
            Logger.confirm("There are no accounts available, please first register  ");
            Logger.err("Login request denied");
        } else {

            for (User user : users) {
                if (user.getUsername().equals(username) && user.getPassword().equals(password)) {
                    currentUser = new User(user.getUsername(), user.getPassword());
                    access = true;
                    Logger.confirm("Login request accepted " + username + " logged in");
                    break;
                } else {
                    access = false;
                    Logger.err(("Login request denied, " + username + " is not able to login"));
                }
            }
        }
        return access;
    }

    public boolean checkValidRegister(String newUserName) {
        boolean exists = false;

        for (User user : users) {
            if (user.getUsername().equals(newUserName)) {
                Logger.err("This username already exists");
                exists = true;
            }
        }
        return exists;
    }

    public void printusers(){
        for (User user : users) {
            System.out.println(user.toString());
        }
    }
}
