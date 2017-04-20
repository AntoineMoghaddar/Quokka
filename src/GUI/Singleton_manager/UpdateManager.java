package GUI.Singleton_manager;

import Design.Logger;
import GUI.JavaFX.Scenes.MainScreen.MessageProcessing.Message_Process;

import javax.swing.*;

/**
 * @author Moose.
 */
public class UpdateManager extends SwingWorker<Object, Object> {

    private Message_Process message_process;

    @Override
    protected Object doInBackground() throws Exception {
        message_process = Message_Process.getInstance();
        do {
            java.awt.EventQueue.invokeLater(() -> {
                Logger.log("updating");
                message_process.readMessagesFile();
                // here the swing update
            });
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        } while (true);
    }
}