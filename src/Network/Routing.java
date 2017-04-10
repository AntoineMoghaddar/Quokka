package Network;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashMap;

/**
 * Created by Rowin on 10-4-2017.
 * Every client has an instance of this class, this class keeps track of routes and determines what route to use
 * Route information needs to be passed to UpdateRoute() whenever new information is received
 */
public class Routing {

    // Destination Address and next hop respectively
    private HashMap<InetAddress, InetAddress> RoutingTable = new HashMap<>();

    private InetAddress localAddress;

    public Routing(InetAddress localaddr) {
        localAddress = localaddr;
    }


    // Returns the next hop for the given destination
    public InetAddress getRoute(InetAddress Destination) {

        for(InetAddress dest : RoutingTable.keySet()) {
            if(dest.equals(Destination)) {
                //Route found in table
                return RoutingTable.get(dest);
            }
        }

        //Route is not in the table
        //@TODO figure out what to do in this situation, probably return null but request routing information
        return null;
    }

    // Gets the information to send to other clients about its routing
    public byte[] generateRoutingData() {
        byte tableSize = 0;
        for(InetAddress dest : RoutingTable.keySet()) {
            tableSize++;
        }
        byte[] data = new byte[1+tableSize*8];
        data[0] = tableSize;

        int counter = 0;
        for(InetAddress dest : RoutingTable.keySet()) {
            System.arraycopy(dest.getAddress(),0,data,counter*8,4);
            System.arraycopy(RoutingTable.get(dest).getAddress(),0,data,counter*8 + 4,4);
            counter++;
        }
        return data;
    }

    // Update the RoutingTable using data from routing packets
    public void updateRoute(byte[] routeData, InetAddress source) {
        //Add source to routing table if we dont have it yet
        if(!RoutingTable.containsKey(source)) {
            RoutingTable.put(source, source);
        }

        // The first byte should represent how many hosts are in the routeData
        InetAddress[] routes = new InetAddress[routeData[0]*2];

        for(int i = 0; i < routeData[0]; i++) {
            byte[] addr = new byte[]{routeData[i*4+1],routeData[i*4+2],routeData[i*4+3],routeData[i*4+4]};
            try {
                routes[i] = InetAddress.getByAddress(addr);
            } catch(UnknownHostException e) {
                //Do nothing
                //@TODO figure out if this is actually bad, im pretty sure its not
            }
        }

        //routes[] now contains routeData[] converted to INetAddresses, with destination and next hop alternating
        int i = 0;
        while(i < routes.length)
        {
            if(routes[i+1].equals(localAddress)) {
                i+=2;
                continue;
            }
            if(RoutingTable.containsKey(routes[i])) {
                if(RoutingTable.get(routes[i]).equals(routes[i+1])) {
                    // nothing changed
                    i+=2;
                    continue;
                }
            }

            if(!RoutingTable.containsKey(routes[i])) {
                //We do not have this destination, apparently this host does
                RoutingTable.put(routes[i], source);
            }

            i+=2;
        }

    }
}
