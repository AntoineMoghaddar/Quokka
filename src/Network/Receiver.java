package Network;

import java.io.*;
import java.net.*;
import java.text.SimpleDateFormat;
import java.util.Calendar;

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
        byte[] inBuf = new byte[1028];
        try {
            //Prepare to join multicast group
            socket = new MulticastSocket(8888);
            InetAddress address = InetAddress.getByName("224.0.0.2");
            NetworkInterface networks = socket.getNetworkInterface();

            socket.joinGroup(address);

            while (true) {
                inPacket = new DatagramPacket(inBuf, inBuf.length );
                socket.receive(inPacket);

                receivePacket(inPacket, socket);
//                System.err.println("Received " + inPacket.getLength() +
//                        " bytes from " + inPacket.getAddress());
//                String msg = new String(inBuf, 0, inPacket.getLength());
//                System.out.println("From " + inPacket.getAddress() + " Msg : " + msg);

            }
        } catch (IOException ioe) {
            System.out.println(ioe);
        }
    }

    public void receivePacket(DatagramPacket receivedPacket, MulticastSocket socket) {
        // Handle Missing packets here

        // Handle the different types of packets here
        switch (receivedPacket.getData()[0]) {
            case 0:
                // Message
                String msg = new String(receivedPacket.getData(), 1, receivedPacket.getLength() - 1);
                System.out.println("From " + receivedPacket.getAddress() + " Msg : " + msg);
                break;
            case 1:
                // Routing Packet
                routing.updateRoute(receivedPacket.getData(), receivedPacket.getAddress());
                break;
            case 2:
                //ACK packet
            case 4:
                System.out.println("received file header");
                receiveFile(socket, receivedPacket);

            default:
                // Wrong packet, discard
                break;
        }

    }

    private void receiveFile(MulticastSocket socket, DatagramPacket packet) {
        try {
            FileOutputStream stream = new FileOutputStream("test1.txt");
            stream.write(packet.getData());
            System.out.println("data received 1" + packet.getData());
            stream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    public String messageTemplate(String text){
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        String newText = "user: " + text + " at: " +sdf.format(cal.getTime());
        return newText;
    }
}
