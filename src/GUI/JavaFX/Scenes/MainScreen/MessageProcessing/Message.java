package GUI.JavaFX.Scenes.MainScreen.MessageProcessing;

import GUI.JavaFX.Scenes.LoginScreen.LoginProcessing.User;

import java.util.Date;

/**
 * @author Moose.
 */
public class Message {

    private User receiver, sender;
    private String message;
    private Date date;

    public Message(User receiver, User sender, String message, Date date) {
        this.receiver = receiver;
        this.sender = sender;
        this.message = message;
        this.date = date;
    }

    public Message(User receiver, User sender, String message) {
        this.receiver = receiver;
        this.sender = sender;
        this.message = message;
    }

    public User getReceiver() {
        return receiver;
    }

    public User getSender() {
        return sender;
    }

    public String getMessage() {
        return message;
    }

    public Date getDate() {
        return date;
    }


    @Override
    public String toString() {
        return "Message{" +
                "receiver=" + receiver +
                ", sender=" + sender +
                ", message='" + message + '\'' +
                ", date=" + date +
                '}';
    }
}
