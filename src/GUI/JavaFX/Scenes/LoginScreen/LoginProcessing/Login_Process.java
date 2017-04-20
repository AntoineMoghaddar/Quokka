package GUI.JavaFX.Scenes.LoginScreen.LoginProcessing;

import Design.Logger;
import GUI.Main.QuokkaModel;
import GUI.Singleton_manager.UpdateManager;
import Helperclasses.Exceptions.LineException;
import org.apache.commons.codec.binary.Hex;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * @author Moose
 * This class is meant to process a login request/ register request but also able to read and write data in a textfile,
 * these methods can be used in, for example, JavaFx projects
 */
public class Login_Process {
    private ArrayList<User> users = new ArrayList<>();
    private User currentUser = null;
    private QuokkaModel quokkaModel;
    private static Login_Process instance;


    private Login_Process() {
        quokkaModel = QuokkaModel.getInstance();
        readUsersFile("userlist.txt");
        UpdateManager updateManagee = new UpdateManager();
        updateManagee.execute();

    }

    public static Login_Process getInstance() {
        if (instance == null) {
            instance = new Login_Process();
        }
        return instance;
    }


    public int readUsersFile(String filename) {
        Logger.notice("Applied for reading in user list");
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
                        Logger.err(le.getMessage() + " on line " + linenumber);
                    }
                }
            }
            Logger.confirm("Read in " + amountObjects + " objects");
        } catch (FileNotFoundException fnf) {
            fnf.printStackTrace();
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
            newUser = new User(lineData[0], lineData[1], lineData[2], lineData[3], lineData[4]);
        } else {
            throw new LineException("Fill in all the blanks");
        }
        Logger.log("User " + newUser.toString() + " processed");
        return newUser;
    }//done

    public void fileWriter(String username, String password, String email, String gender, String key) {
        PrintWriter printWriter;
        Logger.notice("Applied for writing in userlist.txt");
        try {
            printWriter = new PrintWriter(new FileWriter("userlist.txt", true));
            printWriter.write(username + ";" + password + ";" + email + ";" + gender + ";" + key + ";\n");
            printWriter.flush();
            printWriter.close();

        } catch (IOException file) {
            Logger.err(file.getMessage());
        }
    }


    public boolean Register(String username, String password, String email, String gender, String key) {
        boolean notaccessed;

        User newUser = new User(username, password, email, gender, key);

        if (!checkValidRegister(newUser.getUsername())) {
            users.add(newUser);
            Logger.notice("started writing in file");
            fileWriter(username, password, email, gender, key);

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
                    currentUser = user;
//                    = new User(user.getUsername(), user.getPassword());
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

    public User getUser(String username) {
        for (User user : users) {
//            Logger.err(user.toString());
            if (user.getUsername().equals(username)) {
                return user;
            }
        }
        return null;
    }

    /**
     * @purpose debug
     */
    public void printusers(){
        for (User user : users) {
            Logger.debug(user.toString());
        }
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(User currentUser) {
        this.currentUser = currentUser;
    }

    public String SHA512_encoder(String value, String secretkey) {
        try {
            // Get an hmac_sha1 secretkey from the raw secretkey bytes
            byte[] keyBytes = secretkey.getBytes();
            SecretKeySpec signingKey = new SecretKeySpec(keyBytes, "HmacSHA512");

            // Get an hmac_sha512 Mac instance and initialize with the signing secretkey
            Mac mac = Mac.getInstance("HmacSHA512");
            mac.init(signingKey);

            // Compute the hmac on input data bytes
            byte[] rawHmac = mac.doFinal(value.getBytes());

            // Convert raw bytes to Hex
            byte[] hexBytes = new Hex().encode(rawHmac);

            //  Covert array of Hex bytes to a String
            Logger.debug("Hashed Password" + new String(hexBytes, "UTF-8"));
            return new String(hexBytes, "UTF-8");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    /**
     * @return
     */
    public ArrayList<User> getUsers() {
        Logger.confirm("Apply read in Userlist");
        for (User user : users) {
            Logger.notice(user.toString());
        }
        return users;
    }
}
