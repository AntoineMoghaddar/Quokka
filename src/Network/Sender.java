package Network;
import java.io.*;
import java.net.*;
import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Rick on 9-4-2017.
 */
public class Sender implements Runnable {

    private Routing routing;

    public Sender(Routing _routing) {
        routing = _routing;
    }

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
            InetAddress address = InetAddress.getByName("224.0.0.2");
            ip = InetAddress.getLocalHost();
            socket.setInterface(ip);

            long previousSendTime = 0;
            int msgLength;

            while (true) {

                if(System.currentTimeMillis() - previousSendTime > 5000) {
                    //Make our presence known
                    previousSendTime = System.currentTimeMillis();//possible slight delay

                    socket.send(new DatagramPacket(routing.generateRoutingData(), routing.getRoutingDataSize(), address, PORT));
                }

                msg = scan.nextLine();
                counter++;
                msgLength = msg.getBytes().length;
                outBuf = new byte[msgLength + 1];
                outBuf[0] = 0;//Code used to indicate that this is a message
                System.arraycopy(msg.getBytes(),0,outBuf,1, msgLength);
                sendPack = new DatagramPacket(outBuf, msgLength+1, address, PORT);

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