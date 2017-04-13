package GUI.JavaFX.Scenes.LoginScreen.LoginProcessing;

/**
 * Created by Antoine Moghaddar on 30-1-2016.
 */
public class User {

    private String gender;
    private String username;
    private String email;
    private String password;

    //only used for logiing in
    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    //this constructor is only used for registering
    public User(String username, String password, String email, String gender) {

        this.username = username;
        this.email = email;
        this.password = password;
    }

    public String getPassword() {
        return password;
    }


    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    @Override
    public String toString() {
        return "User{" +
                "username='" + username + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
