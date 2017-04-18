package GUI.JavaFX.Scenes.LoginScreen.LoginProcessing;

/**
 * Created by Antoine Moghaddar on 30-1-2016.
 */
public class User {

    private String gender, key, username, email, password;

    //only used for logging in
    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    //this constructor is only used for registering
    public User(String username, String password, String email, String gender, String key) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.gender = gender;
        this.key = key;
    }


    public String getGender() {
        return gender;
    }

    public String getKey() {
        return key;
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
                "gender='" + gender + '\'' +
                ", key='" + key + '\'' +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
