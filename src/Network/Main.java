package Network;
import java.util.Scanner;

/**
 * Created by Rick on 7-4-2017.
 */
public class Main {


    public static void main(String args[]){
        (new Thread(new Sender())).start();
        (new Thread(new Receiver())).start();

    }

}
