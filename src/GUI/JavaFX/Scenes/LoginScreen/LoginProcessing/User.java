package GUI.JavaFX.Scenes.LoginScreen.LoginProcessing;

/**
 * Created by Antoine Moghaddar on 30-1-2016.
 */
public class User {

    private String firstName;
    private String lastName;
    private String username;
    private String email;
    private String password;

    //only used for logiing in
    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    //this constructor is only used for registering
    public User(String firstName, String lastName, String username, String email, String password) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
        this.email = email;
        this.password = password;
    }

    public String getPassword() {
        return password;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
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
