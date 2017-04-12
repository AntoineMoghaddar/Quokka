package Network;

import java.net.InetAddress;
import java.util.HashMap;

/**
 * Created by Rowin on 12-4-2017.
 * This class keeps track of TCP ACKs received and sent, every client has a TCPHandler
 */
public class TCPHandler {

    //Keeps track of this clients ACK number
    private byte AckNumber = 0;

    // Map of ACK it expects next from a certain InetAddress
    private HashMap<InetAddress, Integer> AckExpected = new HashMap<>();

    // Map of last ACK it has received from a certain InetAddress
    private HashMap<InetAddress, Integer> AckReceived = new HashMap<>();

    public void ackReceived(InetAddress source, int AckNumber) {

    }

    // Returns the ackNumber then increments it
    public byte getAckNumber() {
        return AckNumber++;
    }

}
