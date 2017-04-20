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
            Sender sender = new Sender(routing);
            new Thread(sender).start();
            new Thread(new Receiver(routing,sender)).start();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }


    }

}
