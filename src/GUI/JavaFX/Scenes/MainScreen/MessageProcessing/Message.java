package GUI.JavaFX.Scenes.MainScreen.MessageProcessing;

import GUI.JavaFX.Scenes.LoginScreen.LoginProcessing.User;

import java.util.Date;

/**
 * @author Moose
 * @Definition User Object consistent of a single message file record
 * @-> including belonging methods
 */
public class Message {

    private User receiver, sender;
    private String message;
    private Date date;

    /**
     * @param receiver; The receiver of the message
     * @param sender;   The sender of the message
     * @param message;  The actual message itself
     * @param date;     A timestamp in which the message is written
     * @Definition Message constructor
     */
    public Message(User receiver, User sender, String message, Date date) {
        this.receiver = receiver;
        this.sender = sender;
        this.message = message;
        this.date = date;
    }

    /**
     * @param receiver; The receiver of the message
     * @param sender;   The sender of the message
     * @param message;  The actual message itself
     * @Definition Message constructor
     */
    public Message(User receiver, User sender, String message) {
        this.receiver = receiver;
        this.sender = sender;
        this.message = message;
    }

    /**
     * @Definition Getters
     */
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

    /**
     * @return String value of this current object
     * @Definition ToString method
     * @use convert into String value
     */
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
