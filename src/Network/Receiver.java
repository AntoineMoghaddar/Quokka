package Network;

import java.io.*;
import java.net.*;

/**
 * Created by Rick on 9-4-2017.
 */
public class Receiver implements Runnable {

    private Routing routing;

    public Receiver(Routing _routing) {
        routing = _routing;
    }

    public void run() {

        MulticastSocket socket = null;
        DatagramPacket inPacket = null;
        byte[] inBuf = new byte[256];
        try {
            //Prepare to join multicast group
            socket = new MulticastSocket(8888);
            InetAddress address = InetAddress.getByName("224.0.0.2");
            NetworkInterface networks = socket.getNetworkInterface();

            socket.joinGroup(address);

            while (true) {
                inPacket = new DatagramPacket(inBuf, inBuf.length);
                socket.receive(inPacket);

                receivePacket(inPacket);
//                System.err.println("Received " + inPacket.getLength() +
//                        " bytes from " + inPacket.getAddress());
//                String msg = new String(inBuf, 0, inPacket.getLength());
//                System.out.println("From " + inPacket.getAddress() + " Msg : " + msg);
                inPacket.setLength(inBuf.length);
            }
        } catch (IOException ioe) {
            System.out.println(ioe);
        }
    }

    public void receivePacket(DatagramPacket receivedPacket) {
        // Handle Missing packets here

        // Handle the different types of packets here
        switch(receivedPacket.getData()[0]) {
            case 0:
                // Message
                String msg = new String(receivedPacket.getData(),1,receivedPacket.getLength()-1);
                System.out.println("From " + receivedPacket.getAddress() + " Msg : " + msg);
                break;
            case 1:
                // Routing Packet
                routing.updateRoute(receivedPacket.getData(),receivedPacket.getAddress());
                break;
            case 2:
                //ACK packet

            default:
                // Wrong packet, discard
                break;
        }

    }

    private void receiveFile(MulticastSocket socket){
        DatagramPacket packet;

        int packCount = 0;
        byte[] firstPack= new byte[4];
        DatagramPacket firstReceived = new DatagramPacket(firstPack, firstPack.length);
        try {
            socket.setReceiveBufferSize(450);
            socket.receive(firstReceived);
            int fileSize = intToByte(firstPack);
            System.out.println("File size = " + fileSize);
            int numPack = (fileSize / 1028);
            double sendBound = (80 / numPack);
            double strikes = 0;

            for(int i = 0; i < numPack; i++){
                System.out.println(packCount + " < " + numPack);
                strikes += sendBound;
                if (strikes >= 1) {
                    while(strikes >= 1) {
                        System.out.println("|");
                        strikes--;
                    }
                }
                byte[] buf = new byte[1028];
                packet = new DatagramPacket(buf, buf.length);
                socket.receive(packet);
                packCount++;
            }


        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private int intToByte(byte[] b){
        return b[0] << 24 | (b[1] & 0xff) << 16 | (b[2] & 0xff) << 8 | (b[3] & 0xff);
    }

}
