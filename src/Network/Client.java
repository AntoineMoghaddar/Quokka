package Network;

/**
 * Created by Rowin on 11-4-2017.
 */
public class Client {

    private Routing routing;

    public static void main(String args[]){
        (new Thread(new Sender())).start();
        (new Thread(new Receiver())).start();

    }


}
