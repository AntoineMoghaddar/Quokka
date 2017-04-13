package Network;

import Helperclasses.*;
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

            long previousSendTime = 0;
            int msgLength;

            while (true) {
               //sendFile(socket, address);
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
        File test = new File("sneding.test");
        Path path = test.toPath();
        int sendSize = 256; //byte send capacity
        int numPack = 0;
        try {
            byte[] fileArray = Files.readAllBytes(path);
            int fileSize = fileArray.length;
            if((numPack = fileArray.length / sendSize) == 0){
                numPack = 1;
            }else{
                numPack = numPack;
            }
            for(int seq = 0; seq <= numPack; seq++){
                fileArray = Files.readAllBytes(path);
                byte[] buf = new byte[fileArray.length + 6];
                System.arraycopy(fileArray, 0, buf, 6, fileSize);
                buf[0] = (byte) 4;
                byte[] bufSeq = ByteHandler.intToBytes(seq);
                buf[1] = bufSeq[0];
                buf[2] = bufSeq[1];
                byte[] bufNum = ByteHandler.intToBytes(numPack);
                buf[3] = bufNum[0];
                buf[4] = bufNum[1];
                buf[5] = (byte)fileSize;
                if(seq == 0 && numPack == 1){
                    DatagramPacket packet = new DatagramPacket(buf, buf.length, group, PORT);
                    socket.send(packet);
                    System.out.println("File values : " + Arrays.toString(buf));
                } else if(seq == 0 && numPack != 1){
                    byte[] cutArray = Arrays.copyOfRange(buf, 0, sendSize);// might have to be +1
                    DatagramPacket packet = new DatagramPacket(buf, buf.length, group, PORT);
                    socket.send(packet);
                } else if(seq != 0 && numPack != 1){
                    int remain = fileArray.length - (seq * sendSize);
                    if(remain > sendSize){
                        byte[] cutArray = Arrays.copyOfRange(buf, seq * sendSize, seq * 2 * sendSize);
                        DatagramPacket packet = new DatagramPacket(buf, buf.length, group, PORT);
                        socket.send(packet);
                    } else{
                        byte[] cutArray = Arrays.copyOfRange(buf, seq * sendSize, (seq * sendSize) + remain);
                        DatagramPacket packet = new DatagramPacket(buf, buf.length, group, PORT);
                        socket.send(packet);
                    }

                }
            }


        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}