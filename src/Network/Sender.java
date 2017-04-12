package Network;
import javax.sound.sampled.Port;
import javax.xml.crypto.Data;
import java.io.*;
import java.net.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
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

    private static FileInputStream in;

    private static final int PORT = 8888;

    public void run() {

        MulticastSocket socket = null;
        DatagramPacket sendPack = null;
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
                sendFile(socket, address);

                if (System.currentTimeMillis() - previousSendTime > 5000) {
                    //Make our presence known
                    previousSendTime = System.currentTimeMillis();//possible slight delay

                    socket.send(new DatagramPacket(routing.generateRoutingData(), routing.getRoutingDataSize(), address, PORT));
                }

                msg = scan.nextLine();
                counter++;
                msgLength = msg.getBytes().length;
                outBuf = new byte[msgLength + 1];
                outBuf[0] = 0;//Code used to indicate that this is a message
                System.arraycopy(msg.getBytes(), 0, outBuf, 1, msgLength);
                sendPack = new DatagramPacket(outBuf, msgLength + 1, address, PORT);

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

    private void sendFile(MulticastSocket socket, InetAddress group) {
        File test = new File("messages.txt");
        Path path = test.toPath();
        int sendSize = 1024;
        try {
            byte[] fileArray = Files.readAllBytes(path);
            for(int seq = 0; seq < fileArray.length ; seq++) {
                byte[] buf = new byte[fileArray.length + 2];
                buf[0] = 4;
                buf[1] = (byte) seq;
                if (seq == 0 && buf.length <= sendSize) {
                    Arrays.copyOfRange(buf, 0, buf.length );
                    DatagramPacket packet = new DatagramPacket(buf, buf.length, group, PORT);
                    socket.send(packet);
                } else if( seq == 0 && buf.length > sendSize) {
                    Arrays.copyOfRange(buf, 0, sendSize);
                    DatagramPacket packet = new DatagramPacket(buf, buf.length, group, PORT);
                    socket.send(packet);
                }
                else if(buf.length > (seq*2) * sendSize){
                    Arrays.copyOfRange(buf, seq * sendSize, (seq *2) * sendSize);
                    DatagramPacket packet = new DatagramPacket(buf, buf.length, group, PORT);
                }
                else{
                    Arrays.copyOfRange(buf, seq * sendSize, buf.length);
                    DatagramPacket packet = new DatagramPacket(buf, buf.length, group, PORT);
                }
            }
           System.out.println("" + fileArray + " Size; " + fileArray.length);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}