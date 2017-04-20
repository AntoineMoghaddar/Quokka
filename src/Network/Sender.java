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

    private TCPHandler TCP;

    /**
     * @Definition constructor of the sender.
     * @param routing
     */

    public Sender(Routing routing) {
        this.routing = routing;
    }

    private static FileInputStream in;

    private static final int PORT = 8888;

    private int counter;

    private MulticastSocket sock;

    private int sendMax = 256;

    private String file = "File.";

    private String name = "";

    /**
     * @use runs instance of sender and sets up connection
     */
    public void run() {

        MulticastSocket socket = null;
        DatagramPacket sendPack = null;
        Scanner scan = new Scanner(System.in);
        byte[] outBuf;
        InetAddress ip;


        try {
            socket = new MulticastSocket(8888);
            sock = socket;
            String msg;
            System.out.println("Please enter your username");
            name = scan.nextLine();
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

                if (msg.toLowerCase().indexOf(file.toLowerCase()) != -1) {
                    String[] paths = msg.split("File.");
                    System.out.println(Arrays.toString(paths));
                    String path1 = paths[1];
                    sendFile(socket, address, ip, path1);

                } else {
                    msgLength = msg.getBytes().length;
                    outBuf = new byte[msgLength + 1];
                    outBuf[0] = 0;//Code used to indicate that this is a message
                    System.arraycopy(msg.getBytes(), 0, outBuf, 1, msgLength);
                    sendMsg(socket, address, ip, msg);
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

    /**
     * @use Sends a packet to other cliets using multicast.
     * @param socket
     * @param packet
     * @param ip
     */
    public void sendPacket(MulticastSocket socket, DatagramPacket packet, InetAddress ip) {
        try {
            socket.send(packet);
            TCP.packetSent(counter, packet);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * @use Sends a packet but only for TCP purposes
     * @param packet
     */
    // Used by TCPHandler to resend packets
    public void sendPacket(DatagramPacket packet) {
        try {
            sock.send(packet);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * @use Lets the receiver send acks.
     * @param ackNumber
     * @param destination
     */
    public void sendAck(int ackNumber, InetAddress destination) {
        try {
            byte buf[] = new byte[7];
            buf[0] = 2;
            byte[] seq = ByteHandler.intToBytes(ackNumber);
            buf[1] = seq[0];
            buf[2] = seq[1];
            try {
                byte[] ip = InetAddress.getLocalHost().getAddress();
                buf[7] = ip[0];
                buf[8] = ip[1];
                buf[9] = ip[2];
                buf[10] = ip[3];
            } catch (UnknownHostException e) {
                e.printStackTrace();
            }

            DatagramPacket pack = new DatagramPacket(buf, buf.length, destination, PORT);

            sock.send(pack);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * @use Used to forward a packet to other clients, which are unreachable by another client.
     * @param socket
     * @param packet
     * @param originalSource
     * @param destination
     */
    public void forwardPacket(MulticastSocket socket, DatagramPacket packet, InetAddress originalSource, InetAddress destination) {
        packet.setAddress(destination);
        byte[] data = packet.getData();
        byte[] ipAddress = originalSource.getAddress();
        data[7] = ipAddress[0];
        data[8] = ipAddress[1];
        data[9] = ipAddress[2];
        data[10] = ipAddress[3];
        packet.setData(data);
        try {
            socket.send(packet);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * @use To send a file with a given path.
     * @param socket
     * @param group
     * @param ip
     * @param path
     */
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
            if (numPack == 0) {
                numPack = 1;
            } else {
                numPack = numPack;
            }
            for (int seq = 0; seq < numPack; seq++) {
                if (remain >= sendMax - 11) {
                    sendSize = sendMax - 11;
                } else {
                    sendSize = remain;
                }
                //setup header
                sentBytes = sentBytes + sendSize;
                remain = fileArray.length - sentBytes;
                fileArray = Files.readAllBytes(pathFile);
                byte[] buf = new byte[sendSize + 11];
                // [0] packet type, [1][2] seq, [3][4] total packets, [5][6] amount of data bytes, [7-10] address
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
                if (seq == 0 && numPack == 1) {
                    System.arraycopy(fileArray, 0, buf, 11, sendSize);
                    DatagramPacket packet = new DatagramPacket(buf, buf.length, group, PORT);
                    TCP.packetSent(seq, packet);
                    sendPacket(socket, packet, group);
                    sendEndPacket(socket, group, numPack, sendSize, ip);
                } else if (seq == 0 && numPack != 1) {
                    System.arraycopy(fileArray, 0, buf, 11, sendSize);
                    DatagramPacket packet = new DatagramPacket(buf, buf.length, group, PORT);
                    TCP.packetSent(seq, packet);
                    sendPacket(socket, packet, group);
                } else if (seq != 0 && numPack != 1) {
                    if (remain >= sendMax) {
                        System.arraycopy(fileArray, seq * sendMax, buf, 11, sendSize - 1);
                        DatagramPacket packet = new DatagramPacket(buf, buf.length, group, PORT);
                        TCP.packetSent(seq, packet);
                        sendPacket(socket, packet, group);
                    } else {
                        System.arraycopy(fileArray, seq * sendMax, buf, 11, remain - 1);
                        DatagramPacket packet = new DatagramPacket(buf, buf.length, group, PORT);
                        TCP.packetSent(seq, packet);
                        sendPacket(socket, packet, group);
                        sendEndPacket(socket, group, numPack, sendSize, ip);
                    }

                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * @use To send a message after it is formatted into a text file
     * @param socket
     * @param group
     * @param ip
     * @param msg
     */
    private void sendMsg(MulticastSocket socket, InetAddress group, InetAddress ip, String msg) {
        int sendMax = 256; //byte send capacity
        int numPack = 0;
        int sendSize = sendMax;
        int sentBytes = 0;
        int msgLength = msg.getBytes().length;
        byte[] msgArray = new byte[msgLength];
        System.arraycopy(msg.getBytes(), 0, msgArray, 0, msgLength);
        int fileSize = msgArray.length;
        int remain = fileSize;
        numPack = msgArray.length / sendMax;
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
            remain = msgArray.length - sentBytes;
            byte[] buf = new byte[sendSize + 11];
            System.arraycopy(msgArray, 0, buf, 11, msgLength);
            // [0] packet type, [1][2] seq, [3][4] total packets, [5][6] amount of data bytes, [7-10] address
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
                System.arraycopy(msgArray, 0, buf, 11, sendSize);
                DatagramPacket packet = new DatagramPacket(buf, buf.length, group, PORT);
                sendPacket(socket, packet, group);
                TCP.packetSent(seq,packet);
                sendEndPacket(socket, group, numPack, sendSize, ip);
            } else if(seq == 0 && numPack != 1){
                System.arraycopy(msgArray, 0, buf, 11, sendSize);
                DatagramPacket packet = new DatagramPacket(buf, buf.length, group, PORT);
                TCP.packetSent(seq,packet);
                sendPacket(socket, packet, group);
            } else if(seq != 0 && numPack != 1){
                if(remain >= sendMax){
                    System.arraycopy(msgArray, seq * sendMax, buf, 11, sendSize - 1);
                    DatagramPacket packet = new DatagramPacket(buf, buf.length, group, PORT);
                    TCP.packetSent(seq,packet);
                    sendPacket(socket, packet, group);
                } else{
                    System.arraycopy(msgArray, seq * sendMax, buf, 11, remain - 1);
                    DatagramPacket packet = new DatagramPacket(buf, buf.length, group, PORT);
                    TCP.packetSent(seq,packet);
                    sendPacket(socket, packet, group);
                    sendEndPacket(socket, group, numPack, sendSize, ip);
                }

            }
        }

    }

    /**
     * @use send a packet to indicate we are done sending a file.
     * @param socket
     * @param group
     * @param numPack
     * @param sendSize
     * @param ip
     */
    private void sendEndPacket(MulticastSocket socket, InetAddress group, int numPack, int sendSize, InetAddress ip){
        byte[] buf = new byte[11];
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

    }

    /**
     * @use sets tcp.
     * @param tcp
     */
    public void setTCP(TCPHandler tcp) {
        TCP = tcp;
    }
}