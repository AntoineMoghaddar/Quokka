package Network;

import java.net.InetAddress;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Rowin on 12-4-2017.
 * This class keeps track of TCP ACKs received and sent, every client has a TCPHandler
 */
public class TCPHandler {

    private Receiver owningReceiver;

    // Amount of acks needed to conclude that a packet went missing
    private final int  retransmitSpeed = 2;

    //Keeps track of this clients ACK number
    private byte ackNumber = 0;

    // Map of ACKs it expects from a certain InetAddress
    private HashMap<InetAddress, ArrayList<Byte>> ackExpected = new HashMap<>();

    // Map of ACKs it has received from a certain InetAddress, this is used to ask for retransmissions and ignoring duplicate packets
    private HashMap<InetAddress, ArrayList<Byte>> ackReceived = new HashMap<>();

    public TCPHandler(Receiver owner) { owningReceiver = owner; }

    //Call this after getAckNumber() and when a multicast packet is sent
    public void packetSent(){
        //Add previous ackNumber to list of expected Acks for all destinations
        // @TODO: Check if list actually changes in for each loop
        for(List<Byte> list : ackExpected.values()) {
            if(list.indexOf((byte) (ackNumber-1))==-1) { // Check if it is not already in the list
                list.add((byte) (ackNumber-1));
            }
        }
    }

    // Call this after getAckNumber() and when a packet is sent to a single destination
    public void packetSent(InetAddress destination) {
        //Add previous ackNumber to list of expected Acks for this destination
        int index = ackExpected.get(destination).indexOf((byte) (ackNumber-1));
        if(index == -1) {
            // Only add ackNumber-1 to the list if its not already in the list
            ackExpected.get(destination).add((byte) (ackNumber - 1));
        }
    }

    public void ackReceived(InetAddress source, byte ackNumberReceived) {
        // Remove ackNumberReceived from the list of ackExpected
        int index = ackExpected.get(source).indexOf(ackNumberReceived);
        if(index == -1) {
            // Already removed
        } else {
            ackExpected.get(source).remove(index);
        }

        // Add ackNumberReceived to the list of ackReceived if is not there already
        if(ackReceived.get(source).indexOf(ackNumberReceived)==-1) {
            ackReceived.get(source).add(ackNumberReceived);
        }
    }

    // Note this method could be added to ackReceived() but this is easier to read/comprehend
    // Use this to check if a received packet is a duplicate, resend ACK. DO NOT USE ON ACKS packets!
    public boolean isDuplicate(InetAddress source, byte ackNumberReceived) {
        if(ackReceived.get(source).indexOf(ackNumberReceived)!=-1) {
            // ackNumberReceived is in the list, so duplicate resend ACK
            return true;
        }
        return false;
    }


    // Returns the ackNumber for the next packet
    public byte getAckNumber() {
        ackNumber++;
        return (byte) --ackNumber;
    }

}
