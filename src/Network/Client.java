package Network;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * Created by Rowin on 11-4-2017.
 */
public class Client {

    private static Routing routing;

    public static void main(String args[]){
        try {
            routing = new Routing(InetAddress.getLocalHost());
            System.out.println(InetAddress.getLocalHost());
            (new Thread(new Sender(routing))).start();
            (new Thread(new Receiver(routing))).start();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }



    }


}
