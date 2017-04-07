package Network;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.util.Scanner;

/**
 * Created by Rick on 7-4-2017.
 */
public class NetworkModel {


    public NetworkModel(){
        try {
            Scanner scan = new Scanner(System.in);
            System.out.println("Please enter username");
            String name = scan.nextLine();
            System.out.println("Welcome " + name + " connection is being made...");
            InetAddress group = InetAddress.getByName("228.5.6.7");
            MulticastSocket socket = new MulticastSocket(6666);
            socket.joinGroup(group);
            DatagramPacket setup = new DatagramPacket(name.getBytes(), name.length(),
                    group, 6666);
            socket.send(setup);
            System.out.println("Connection has been made you can start chatting");
            while (true){
                sendMsg(socket, group, scan);

            }
        }
        catch(IOException e){
            e.printStackTrace();
        }
    }

    public void sendMsg(MulticastSocket socket, InetAddress group, Scanner scan){
        String txt =  scan.nextLine();
        DatagramPacket msg = new DatagramPacket(txt.getBytes(), txt.length(), group, 6666);
        try {
            socket.send(msg);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}