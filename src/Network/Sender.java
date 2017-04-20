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
import java.util.concurrent.TimeUnit;


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

    private int counter;

    int sendMax = 256;

    private String file  = "File.";

    public void run() {

        MulticastSocket socket = null;
        DatagramPacket sendPack = null;
        Scanner scan = new Scanner(System.in);
        byte[] outBuf;
        InetAddress ip;


        try {
            socket = new MulticastSocket(8888);
            String msg;
            System.out.println("Please enter your username");
            String name = scan.nextLine();
            System.out.println("Welcome " + name + " you are free to chat");
            InetAddress address = InetAddress.getByName("224.0.0.2");
            ip = InetAddress.getLocalHost();

            long previousSendTime = 0;
            int msgLength;

            while (true) {
                if (System.currentTimeMillis() - previousSendTime > 5000) {
                    //Make our presence known
                    previousSendTime = System.currentTimeMillis();//possible slight delay
                    socket.send(new DatagramPacket(routing.generateConnectedClientsData(), routing.getConnectedClientsDataSize(), address, PORT));
                }

                msg = scan.nextLine();

                if (msg.toLowerCase().indexOf(file.toLowerCase()) != -1 ) {
                    String[] path = msg.split(".");
                    sendFile(socket, address, ip, path[0]);

                } else {
                    msgLength = msg.getBytes().length;
                    outBuf = new byte[msgLength + 1];
                    outBuf[0] = 0;//Code used to indicate that this is a message
                    System.arraycopy(msg.getBytes(), 0, outBuf, 1, msgLength);
                    sendPack = new DatagramPacket(outBuf, msgLength + 1, address, PORT);
                    sendPacket(socket, sendPack, ip);
                }


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

    public void sendPacket(MulticastSocket socket, DatagramPacket packet, InetAddress ip){
        try {
            // [0] packet type, [1][2] seq, [3][4] total packets, [5][6] amount of data bytes, [7-10] address
            byte[] buf = packet.getData();
            byte[] res = new byte[buf.length];
           /* res[0] = 4;
            byte[] seq = ByteHandler.intToBytes(counter);
            res[1] = seq[0];
            res[2] = seq[1];
            int fileSize = res.length/sendMax;
            byte[] numPack = ByteHandler.intToBytes(fileSize);
            res[3] = numPack[0];
            res[4] = numPack[1];
            byte[] bytes = ByteHandler.intToBytes(buf.length);
            res[5] = bytes[0];
            res[6] = bytes[1];
            byte[] ipAddress = ip.getAddress();
            res[7] = ipAddress[0];
            res[8] = ipAddress[1];
            res[9] = ipAddress[2];
            res[10] = ipAddress[3];*/
            socket.send(packet);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void forwardPacket(MulticastSocket socket, DatagramPacket packet, InetAddress originalSource, InetAddress destination) {
        packet.setAddress(destination);
        byte[] data = packet.getData();
        byte[] ipAddress = originalSource.getAddress();
        data[7] = ipAddress[0];
        data[8] = ipAddress[1];
        data[9] = ipAddress[2];
        data[10] = ipAddress[3];
        packet.setData(data);
        try{
        socket.send(packet);
    } catch (IOException e) {
        e.printStackTrace();
    }

    }

    //sending file
    private void sendFile(MulticastSocket socket, InetAddress group, InetAddress ip, String path) {
        File test = new File(path);
        Path pathFile = test.toPath();
        int sendMax = 256; //byte send capacity
        int numPack = 0;
        int sendSize = sendMax;
        int sentBytes = 0;
        try {
            byte[] fileArray = Files.readAllBytes(pathFile);
            int fileSize = fileArray.length;
            int remain = fileSize;
            numPack = fileArray.length / sendMax;
            System.out.println(numPack);
            if(numPack  == 0){
                numPack = 1;
            }else{
                numPack = numPack;
            }
            for(int seq = 0; seq < numPack; seq++){
                if(remain >= sendMax - 11){
                    sendSize = sendMax - 11;
                }else{
                    sendSize = remain;
                }
                //setup header
                sentBytes = sentBytes + sendSize;
                remain = fileArray.length - sentBytes;
                fileArray = Files.readAllBytes(pathFile);
                byte[] buf = new byte[sendSize + 11];
                buf[0] = 4;
                byte[] bufSeq = ByteHandler.intToBytes(seq);
                buf[1] = bufSeq[0];
                buf[2] = bufSeq[1];
                byte[] bufNum = ByteHandler.intToBytes(numPack);
                buf[3] = bufNum[0];
                buf[4] = bufNum[1];
                byte[] byteSize = ByteHandler.intToBytes(sendSize);
                buf[5] = byteSize[0];
                buf[6] = byteSize[1];
                byte[] ipAddress = ip.getAddress();
                buf[7] = ipAddress[0];
                buf[8] = ipAddress[1];
                buf[9] = ipAddress[2];
                buf[10] = ipAddress[3];
                //send packets with appropriate length
                if(seq == 0 && numPack == 1){
                    System.arraycopy(fileArray, 0, buf, 11, sendSize);
                    DatagramPacket packet = new DatagramPacket(buf, buf.length, group, PORT);
                    sendPacket(socket, packet, group);
                } else if(seq == 0 && numPack != 1){
                    System.arraycopy(fileArray, 0, buf, 11, sendSize);
                    DatagramPacket packet = new DatagramPacket(buf, buf.length, group, PORT);
                    sendPacket(socket, packet, group);
                } else if(seq != 0 && numPack != 1){
                    if(remain >= sendMax){
                        System.arraycopy(fileArray, seq * sendMax, buf, 11, sendSize - 1);
                        DatagramPacket packet = new DatagramPacket(buf, buf.length, group, PORT);
                        sendPacket(socket, packet, group);
                    } else{
                        System.arraycopy(fileArray, seq * sendMax, buf, 11, remain - 1);
                        DatagramPacket packet = new DatagramPacket(buf, buf.length, group, PORT);
                        sendPacket(socket, packet, group);
                    }

                }
            }
            byte[] buf = new byte[7];
            buf[0] = 4;
            byte[] bufSeq = ByteHandler.intToBytes(numPack);
            buf[1] = bufSeq[0];
            buf[2] = bufSeq[1];
            byte[] bufNum = ByteHandler.intToBytes(numPack);
            buf[3] = bufNum[0];
            buf[4] = bufNum[1];
            byte[] byteSize = ByteHandler.intToBytes(sendSize);
            buf[5] = byteSize[0];
            buf[6] = byteSize[1];
            byte[] ipAddress = ip.getAddress();
            buf[7] = ipAddress[0];
            buf[8] = ipAddress[1];
            buf[9] = ipAddress[2];
            buf[10] = ipAddress[3];
            DatagramPacket endPacket = new DatagramPacket(buf, buf.length, group, PORT);
            sendPacket(socket, endPacket, group);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}