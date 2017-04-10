package Network;
import java.io.*;
import java.net.*;
import java.util.Scanner;

/**
 * Created by Rick on 9-4-2017.
 */
public class Sender implements Runnable {

    public void run() {

        MulticastSocket socket = null;
        DatagramPacket sendPack = null;
        final int PORT = 8888;
        Scanner scan = new Scanner(System.in);
        byte[] outBuf;
        InetAddress ip;

        try {
            socket = new MulticastSocket(8888);
            long counter = 0;
            String msg;
            System.out.println("Please enter your username");
            String name = scan.nextLine();
            System.out.println("Welcome " + name + " you are free to chat");
            InetAddress address = InetAddress.getByName("224.2.2.3");
            ip = InetAddress.getLocalHost();
            socket.setInterface(ip);

            while (true) {

                msg = scan.nextLine();
                counter++;
                outBuf = msg.getBytes();
                sendPack = new DatagramPacket(outBuf, outBuf.length, address, PORT);

                socket.send(sendPack);

                System.out.println(name + " : " + msg);
                try {
                    Thread.sleep(500);
                } catch (InterruptedException ie) {
                }
            }
        } catch (IOException ioe) {
            System.out.println(ioe);
        }
    }
}