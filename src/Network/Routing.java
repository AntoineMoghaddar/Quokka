package Network;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.ArrayList;

/**
 * Created by Rowin on 18-4-2017.
 * This checks if other clients need their packets forwarded to reach every client
 */
public class Routing {

    private int ackNumber = 0;
    private int connectedClientsDataSize = 0;
    // List of al directly connected clients
    private ArrayList<InetAddress> ConnectedClients = new ArrayList<>();

    // Keeps track of which packets need to be forwarded
    private HashMap<InetAddress, ArrayList<InetAddress>> ForwardingTable;

    private InetAddress localAddress;

    public Routing(InetAddress _localAddress) {
        localAddress = _localAddress;
    }

    /*
    * data : ConnectedClients converted to byte[]
    * source : source of the packet
    * */
    public void updateForwarding(byte[] data, InetAddress source) {
        if(!ConnectedClients.contains(source)) {
            ConnectedClients.add(source);
        }


        ArrayList<InetAddress> hosts = new ArrayList<>();
        for(int i = 0; i < data.length/4; i++) {
            byte[] buf = new byte[4];
            buf[0] = data[i];
            buf[1] = data[i+1];
            buf[2] = data[i+2];
            buf[3] = data[i+3];
            try{
                hosts.add(InetAddress.getByAddress(buf));
            } catch(UnknownHostException e) {

            }
        }

        // Delete the forwarding table information for this source
        ForwardingTable.remove(source);

        // Update the forwarding table
        for(InetAddress address : ConnectedClients) {
            if(!hosts.contains(address)) {
                // This client does not have this address, so we forward it for them.
                ForwardingTable.get(source).add(address);
            }
        }

    }

    // Returns the addresses a packet from source needs to be forwarded to
    // Return null if there is none
    public ArrayList<InetAddress> ForwardAddresses(InetAddress source) {
        if(ForwardingTable.containsKey(source)){
            return ForwardingTable.get(source);
        } else {
            return null;
        }
    }

    // Send this data to other clients for forwarding purposes
    public byte[] generateConnectedClientsData(){
        // Send all ConnectedClients, other clients can figure out Forwarding locally
        //note 1 InetAddress = 4 bytes
        byte[] res = new byte[ConnectedClients.size()*4];

        for(int i = 0; i < ConnectedClients.size(); i++) {
            System.arraycopy(ConnectedClients.get(i), 0,res, i*4,4);
        }
        connectedClientsDataSize = res.length;
        return res;
    }

    public int getConnectedClientsDataSize() { return connectedClientsDataSize; }

    public int getAckNumber(){
        ackNumber++;
        return ackNumber-1;
    }


}
