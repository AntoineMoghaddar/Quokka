package GUI.JavaFX.Scenes.LoginScreen.LoginProcessing;

/**
 * @author Moose
 * @Definition User Object consistent of a single user file record
 * @-> including belonging methods
 */
public class User {

    private String gender, key, username, email, password;

    /**
     * @param username; The given username
     * @param password; The given password
     * @Definition Login
     */
    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    /**
     * @param username; The Name of the current user
     * @param password; A (Hmac-SHA512) hashed version of the user's password; this password can't ever be retrieved in normal form
     * @param email;    The current user his mail
     * @param gender;   The current user his/her gender
     * @param key;      The automatic generated hash secret key
     * @Definition Register form
     */
    public User(String username, String password, String email, String gender, String key) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.gender = gender;
        this.key = key;
    }

    /**
     * @Definition Getters
     */
    public String getGender() {
        return gender;
    }

    public String getKey() {
        return key;
    }

    String getPassword() {
        return password;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    /**
     * @Definition ToString method
     * @use convert into String value
     * @return String value of this current object
     */
    @Override
    public String toString() {
        return "User{" +
                "gender='" + gender + '\'' +
                ", key='" + key + '\'' +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
