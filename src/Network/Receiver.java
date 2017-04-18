package Network;

import java.io.*;
import java.net.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import Helperclasses.ByteHandler;

/**
 * Created by Rick on 9-4-2017.
 */
public class Receiver implements Runnable {

    private Sender ownSender;

    private Routing routing;

    public Receiver(Routing _routing, Sender ownSender) {
        routing = _routing;
        this.ownSender = ownSender;
    }

    private byte[] file;

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
                inPacket = new DatagramPacket(inBuf, inBuf.length );
                socket.receive(inPacket);

                receivePacket(inPacket, socket);
//                System.err.println("Received " + inPacket.getLength() +
//                        " bytes from " + inPacket.getAddress());
//                String msg = new String(inBuf, 0, inPacket.getLength());
//                System.out.println("From " + inPacket.getAddress() + " Msg : " + msg);s
            }
        } catch (IOException ioe) {
            System.out.println(ioe);
        }
    }

    public void receivePacket(DatagramPacket receivedPacket, MulticastSocket socket) {
        // Add the source of receivedPacket to list of ConnectedClients
        routing.addConnectedClient(receivedPacket.getAddress());

        InetAddress originalSource = ByteHandler.byteToInet(Arrays.copyOfRange(receivedPacket.getData(), 7,10));

        // Forward packet if needed
        if(!routing.forwardAddresses(originalSource).isEmpty()) {
            ArrayList<InetAddress> list = routing.forwardAddresses(originalSource);
            for(InetAddress dest : list) {
                ownSender.forwardPacket(socket,receivedPacket, originalSource, dest);
            }
        }



        // Handle the different types of packets here
        switch (receivedPacket.getData()[0]) {
            case 0:
                // Message
                String msg = new String(receivedPacket.getData(), 1, receivedPacket.getLength() - 1);
                System.out.println("From " + originalSource + " Msg : " + msg);
                break;
            case 1:
                // Routing
                routing.updateForwarding(receivedPacket.getData(), receivedPacket.getAddress());
                break;
            case 2:
                //ACK packet
            case 4:
                receiveFile(socket, receivedPacket);

            default:
                // Wrong packet, discard
                break;
        }

    }

    private void receiveFile(MulticastSocket socket, DatagramPacket packet) {
        try {
            byte[] buf = packet.getData();
            byte[] res = null;

            int numPack = ((buf[4] & 0xff) << 8) | (buf[3] & 0xff);
            int seq = ((buf[2] & 0xff) << 8) | (buf[1] & 0xff);
            int bytes = ((buf[6] & 0xff) << 8) | (buf[5] & 0xff);
            System.out.println("bytes " + bytes);
            FileOutputStream stream = new FileOutputStream("test2.txt");
            System.out.println("seq = " + seq);
                if(seq < numPack) {
                    if (seq == 0) {
                        file = new byte[bytes];
                        System.arraycopy(buf, 7, file, 0, bytes);
                        return;
                    } else {
                        res = new byte[file.length];
                        System.arraycopy(file, 0, res, 0, file.length);
                        file = new byte[res.length + bytes];
                        System.arraycopy(res, 0, file, 0, res.length);
                        System.arraycopy(file, res.length, buf, 7, bytes);
                        return;
                    }
                }else {
                    stream.write(file);
                    stream.flush();
                    stream.close();
                }

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
