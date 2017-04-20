package GUI.JavaFX.Scenes.LoginScreen.LoginProcessing;

import Design.Logger;
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
    private static Login_Process instance;


    /**
     * @Definition Constructor of this class
     * @use Used in a singleton method
     * @-> Only one instance of this method can be used through the entire project.
     */
    private Login_Process() {
        readUsersFile();
        UpdateManager updateManagee = new UpdateManager();
        updateManagee.execute();

    }

    /**
     * @return new instance or already existing instance of this class
     * @Definition initializer for setting up the instance used in this project
     * @use Singleton method initializer
     */
    public static Login_Process getInstance() {
        if (instance == null) {
            instance = new Login_Process();
        }
        return instance;
    }

    /**
     * @Definition File reader for reading in the user list
     * @use a single file gets read in in order to load in all the users
     * @improvements this could be done more general
     * @-> by making it more general you could give a file name as a parameter and read it in as well
     * @-> Making it more general would have made our program more clean and would prevent redundant code.
     * @-> This amount of objects refers to the amount of system users.
     */
    private void readUsersFile() {
        Logger.notice("Applied for reading in user list");
        int amountObjects = 0;
        try {
            Scanner fileScanner = new Scanner(new File("userlist.txt"));
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
    }


    /**
     * @param line; the given line by @readUserFile
     * @return returns a new created user
     * @throws LineException; thrown if a line gives an error because it is non-existent etc
     * @Definition line processor of the filereader
     * @use this method processes the formatting of the given file and converts every line into an object.
     * @-> this new user is being add to a list in @readUserFile
     */
    private User processLineUserFile(String line) throws LineException {
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

    /**
     * @param username; The Name of the current user
     * @param password; A (Hmac-SHA512) hashed version of the user's password; this password can't ever be retrieved in normal form
     * @param email;    The current user his mail
     * @param gender;   The current user his/her gender
     * @param key;      The automatic generated hash secret key
     * @Definition A file is located and will be written in.
     * @use the method retrieves the file location and writes the data in the given format in the file
     * @-> The secret key is automatically generated and will be used to XOR with the given "password" by using SHA512
     */
    private void fileWriter(String username, String password, String email, String gender, String key) {
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


    /**
     * @param username ; The Name of the current user
     * @param password ; A (Hmac-SHA512) hashed version of the user's password; this password can't ever be retrieved in normal form
     * @param email ;    The current user his mail
     * @param gender ;   The current user his/her gender
     * @param key ;      The automatic generated hash secret key
     * @Definition The register form of the system
     * @use As a user you have to give your data and that certain data will be processed here into an existing user
     */
    public void Register(String username, String password, String email, String gender, String key) {

        User newUser = new User(username, password, email, gender, key);

        if (!checkValidRegister(newUser.getUsername())) {
            users.add(newUser);
            Logger.notice("started writing in file");
            fileWriter(username, password, email, gender, key);

        } else {
            Logger.err("Try again");
        }
    }

    /**
     * @param username; The user's username; this has to be a registered username
     * @param password; The user's password; this has to be exactly the same as the registered password
     * @return given access or not
     * @Definition the login form for the users
     * @use Users who want to log in have to enter their information into the fields
     * @-> this information is being processed
     * @-> after this is accepted and the data corresponds the that certain user, that user may login and view his/her data
     * @-> if the password is not exactly the same tha hash won't correspond the by the user given data and thus he/she will not be able to log in
     */
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

    /**
     * @param newUserName; if the user does not exist a new account may be made, in every other case false will be returned
     * @return valid register or not
     * @Definition checks whether the data corresponds to each other
     */
    private boolean checkValidRegister(String newUserName) {
        boolean exists = false;

        for (User user : users) {
            if (user.getUsername().equals(newUserName)) {
                Logger.err("This username already exists");
                exists = true;
            }
        }
        return exists;
    }

    /**
     * @param username; The given username
     * @return a specific user based on the given username
     * @Definition returns a certain user
     * @use A username is given and will be searched for in the list
     */
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
     * @Definition All users are being printed
     * @purpose debug
     */
    public void printusers(){
        for (User user : users) {
            Logger.debug(user.toString());
        }
    }

    /**
     * @return the current user is returned
     * @Definition gives the current user his data
     */
    public User getCurrentUser() {
        return currentUser;
    }

    /**
     * @param currentUser; the Current user who has logged in
     * @Definition A new current user is set
     * @use at login the new current user is being defined and set
     */
    public void setCurrentUser(User currentUser) {
        this.currentUser = currentUser;
    }

    /**
     * @param value;     password combination unXORed with the hascode (created by getPassword())
     * @param secretkey; the secret key provided by the client
     * @return returns a perfectly fine API hash which is ready to be send as a header request
     * @Definition The used encoder for creating the required hash by the CCVShop API
     * @description XORes and Hashes the full API password provided by another method with SHA512
     * @--> and the provided secret key by the client
     * @use XORes the provided password combination (created in getPassword()) with the secret key
     * @--> and hashes it all into one SHA512 encoded String
     */
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
     * @Definition gives a complete arraylist with all the registered users
     * @return The entire arraylist consistent of all users
     */
    public ArrayList<User> getUsers() {
        Logger.confirm("Apply read in Userlist");
        for (User user : users) {
            Logger.notice(user.toString());
        }
        return users;
    }
}
