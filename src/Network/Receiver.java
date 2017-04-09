package Network;

import java.io.*;
import java.net.*;

/**
 * Created by Rick on 9-4-2017.
 */
public class Receiver implements Runnable {

    public void run() {
        MulticastSocket socket = null;
        DatagramPacket inPacket = null;
        byte[] inBuf = new byte[256];
        try {
            //Prepare to join multicast group
            socket = new MulticastSocket(8888);
            InetAddress address = InetAddress.getByName("224.2.2.3");
            socket.joinGroup(address);

            while (true) {
                inPacket = new DatagramPacket(inBuf, inBuf.length);
                socket.receive(inPacket);
                String msg = new String(inBuf, 0, inPacket.getLength());
                System.out.println("From " + inPacket.getAddress() + " Msg : " + msg);
            }
        } catch (IOException ioe) {
            System.out.println(ioe);
        }
    }
}
