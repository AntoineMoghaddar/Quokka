package GUI.JavaFX.Scenes.MainScreen;

import Design.Logger;
import GUI.JavaFX.Scenes.LoginScreen.LoginProcessing.Login_Process;
import GUI.JavaFX.Scenes.LoginScreen.LoginProcessing.User;
import GUI.JavaFX.Scenes.MainScreen.MessageProcessing.Message;
import GUI.JavaFX.Scenes.MainScreen.MessageProcessing.Message_Process;
import GUI.Singleton_manager.ClassManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuBar;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;

import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

/**
 * @author Moose.
 */
public class MainScreenController implements Initializable {

    @FXML
    private MenuBar menuBar;

    @FXML
    private ListView ListViewMain, MessageViewList;

    @FXML
    private TextField writeMessage;

    @FXML
    private Button sendButton;
    private Message_Process message_process;
    private Login_Process login_process;
    private ObservableList<String> messages;
    private ObservableList<String> currentMessage;
    private static int index = 0;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        currentMessage = FXCollections.observableArrayList();
        message_process = Message_Process.getInstance();
        ObservableList<String> users = FXCollections.observableArrayList();
        messages = FXCollections.observableArrayList();
        login_process = Login_Process.getInstance();
        menuBar.getMenus().setAll(ClassManager.getMenuBar().getMenus());

        for (User user : login_process.getUsers()) {
            users.add(user.getUsername());
        }
        ListViewMain.setItems(users);

        writeMessage.setOnKeyPressed(event -> {
            if (event.getCode().equals(KeyCode.ENTER)) {
                sendManager();
            }
        });

        sendButton.setOnAction(event -> {
            sendManager();
        });

    }

    public void sendManager() {
        if (!(writeMessage.getText().isEmpty())) {
            if (ListViewMain.getSelectionModel().getSelectedItem() != null) {
                message_process.fileWriter(login_process.getCurrentUser().getUsername(),
                        login_process.getUser(ListViewMain.getSelectionModel().getSelectedItem().toString()).getUsername(),
                        writeMessage.getText());

                Logger.debug("Reached SendManager");
                for (Message personalMessage : message_process.getMessages()) {
                    Logger.debug("Reached forloop");
                    if (ListViewMain.getSelectionModel().getSelectedItem().equals(personalMessage.getReceiver().getUsername())
                            && Objects.equals(personalMessage.getSender().getUsername(), login_process.getCurrentUser().getUsername())) {
                        Logger.err("value of personal: " + personalMessage.getReceiver().getUsername());
                        Logger.err("value of selected receiver: " + ListViewMain.getSelectionModel().getSelectedItem().toString());


                        Message messagecurr = new Message(login_process.getCurrentUser(),
                                login_process.getUser(ListViewMain.getSelectionModel().getSelectedItem().toString()),
                                writeMessage.getText());

                        message_process.addMessages(messagecurr);
                        writeMessage.setText("");
                        processMessage(messagecurr);
                        index++;
                    }
                }

            } else {
                Logger.err("no data has been selected to send the message to");
            }
        }
    }

    public void processMessage(Message messagecurr) {
        if (index == 0) {
            for (Message message : message_process.getMessages()) {
                messages.add(message.getMessage());
            }
            MessageViewList.setItems(messages);

        } else {
            currentMessage.add(messagecurr.getMessage());
            MessageViewList.setItems(currentMessage);
            currentMessage.removeAll();
        }
    }
}
