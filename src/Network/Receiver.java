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
        // Handle receiving packets here, different types of packets need to be handled differently
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

            default:
                // Wrong packet, discard
                break;
        }

    }

}
