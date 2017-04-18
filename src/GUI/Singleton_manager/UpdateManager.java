package GUI.Singleton_manager;

import Design.Logger;
import GUI.JavaFX.Scenes.LoginScreen.LoginProcessing.Login_Process;

import javax.swing.*;

/**
 * @author Moose.
 */
public class UpdateManager extends SwingWorker<Object, Object> {

    private Login_Process login_process;

    @Override
    protected Object doInBackground() throws Exception {
        login_process = Login_Process.getInstance();
        while (true) {
            java.awt.EventQueue.invokeLater(() -> {
                Logger.log("updating");
                login_process.readUsersFile("userlist.txt");
                // here the swing update
            });
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}