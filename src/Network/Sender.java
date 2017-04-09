package Network;
import java.io.*;
import java.net.*;
import java.util.Scanner;

/**
 * Created by Rick on 9-4-2017.
 */
public class Sender implements Runnable {

    public void run() {
        DatagramSocket socket = null;
        DatagramPacket sendPack = null;
        byte[] outBuf;
        final int PORT = 8888;
        Scanner scan = new Scanner(System.in);

        try {
            socket = new DatagramSocket();
            long counter = 0;
            String msg;

            while (true) {
                msg = scan.nextLine();
                counter++;
                outBuf = msg.getBytes();

                //Send to multicast IP address and port
                InetAddress address = InetAddress.getByName("224.2.2.3");
                sendPack = new DatagramPacket(outBuf, outBuf.length, address, PORT);

                socket.send(sendPack);

                System.out.println("Server sends : " + msg);
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