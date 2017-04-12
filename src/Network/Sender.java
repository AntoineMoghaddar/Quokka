package Network;
import javax.xml.crypto.Data;
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

    private static FileInputStream in;

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
        System.out.println("File sending started from : " + test.getPath());
        //intial pack
        int fileSize = (int) test.length();
        double numPack = (fileSize / 1028);
        System.out.println("" + numPack);
        double sendBound = (80 / numPack);
        double strikes = 0;
        byte[] firstPack = new byte[4];
        intToBytAarr(fileSize, firstPack);
        DatagramPacket firstSend = new DatagramPacket(firstPack, firstPack.length, group, 8888);
        try {
            socket.send(firstSend);
        } catch (IOException e) {
            e.printStackTrace();
        }

        //rest of packs
        try {
            in = new FileInputStream(test);
            byte[] data = new byte[1028];
            int seq = 0;
            int numBytes = 0;
            int sentBytes = 0;

            while ((numBytes = in.read(data)) != -1) {
                seq++;
                strikes += sendBound;
                if (strikes >= 1) {
                    for (int i = 0; i < (int) strikes; i++) {
                        System.out.println("|");
                        strikes--;
                    }
                }

            }
            byte[] dataseq = new byte[4];
            intToBytAarr(seq, dataseq);
            sentBytes += numBytes;

            //data + sequence
            byte[] dataPlusSeq = new byte[dataseq.length +data.length];
            System.arraycopy(dataseq, 0, dataPlusSeq, 0, dataseq.length);
            System.arraycopy(data, 0, dataPlusSeq, dataseq.length, data.length);

            //send
            DatagramPacket toSend = new DatagramPacket(dataPlusSeq, dataPlusSeq.length);
            socket.send(toSend);

        } catch (FileNotFoundException e1) {
            e1.printStackTrace();
        } catch (IOException e1) {
            e1.printStackTrace();
        }

    }

    public static void intToBytAarr(int number, byte[] data) {
        for (int i = 0; i < 4; ++i) {
            int shift = i << 3; // i * 8
            data[3 - i] = (byte) ((number & (0xff << shift)) >>> shift);
        }

    }
}