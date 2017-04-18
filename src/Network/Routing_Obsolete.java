package Network;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.ArrayList;

/**
 * Created by Rowin on 10-4-2017.
 * Every client has an instance of this class, this class keeps track of routes and determines what route to use
 * Route information needs to be passed to UpdateRoute() whenever new information is received
 */
public class Routing_Obsolete {

    // Destination Address and next hop respectively
    private HashMap<InetAddress, InetAddress> RoutingTable = new HashMap<>();

    // Source and Destination address of the packet, Determines if it needs to be forwarded
    private HashMap<InetAddress, InetAddress> ForwardingTable = new HashMap<>();

    private InetAddress localAddress;

    private int routingDataSize;

    private final int headerSize = 1; // amount of extra bytes added to routeData[]

    public Routing_Obsolete(InetAddress localaddr) {
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

        //null means either we dont have the route, or it doesnt need to be forwarded
        return null;
    }

    // Gets the information to send to other clients about its routing
    public byte[] generateRoutingData() {
        int tableSize = 0;
        for(InetAddress dest : RoutingTable.keySet()) {
            tableSize++;
        }
        byte[] data = new byte[headerSize+tableSize*8];
        data[0] = 1;

        int counter = 0;
        for(InetAddress dest : RoutingTable.keySet()) {
            System.arraycopy(dest.getAddress(),0,data,counter*8+headerSize,4);
            System.arraycopy(RoutingTable.get(dest).getAddress(),0,data,counter*8 + 4 + headerSize,4);
            counter++;
        }
        routingDataSize = data.length;
        return data;
    }

    public int getRoutingDataSize() {
        return routingDataSize;
    }

    // Update the RoutingTable using data from routing packets
    public void updateRoute(byte[] routeData, InetAddress source) {
        //Add source to routing table if we dont have it yet
        if(!RoutingTable.containsKey(source)) {
            RoutingTable.put(source, source);
        }

        HashMap<InetAddress, InetAddress> routes = routeDataToHashMap(routeData);



        for(HashMap.Entry<InetAddress, InetAddress> entry : routes.entrySet()) {
            if(entry.getKey().equals(localAddress)) {
                // Ignore if we are the destination
                continue;
            }
            if(RoutingTable.containsKey(entry.getKey()) && RoutingTable.get(entry.getKey()).equals(entry.getValue())) {
                // Ignore if nothing has changed
                continue;
            }

            if(!RoutingTable.containsKey(entry.getKey()) && !entry.getValue().equals(localAddress)) {
                // Add route if we dont have the route and if we are not the nexthop
                RoutingTable.put(entry.getKey(),source);
            }
        }

    }


    public static HashMap<InetAddress,InetAddress> routeDataToHashMap(byte[] routeData) {
        HashMap<InetAddress,InetAddress> res = new HashMap<>();

        //Convert from byte[] to InetAddress[]
        int addresses = routeData.length / 4;
        ArrayList<InetAddress> hosts = new ArrayList<>();
        InetAddress[] Inet = new InetAddress[addresses];
        for(int i = 0; i < addresses; i++) {
            buf[0]=routeData[0+i*4];
            buf[1]=routeData[1+i*4];
            buf[2]=routeData[2+i*4];
            buf[3]=routeData[3+i*4];

            try {
                Inet[i] = InetAddress.getByAddress(buf);
            } catch (UnknownHostException e) {
                //@TODO figure out if this is actually bad, im pretty sure its not
            }
        }

        //InetAddress[] to hashmap
        for(int i = 0; i < Inet.length / 2; i++) {
            res.put(Inet[i], Inet[i+1]);
        }

        return res;
    }
}
