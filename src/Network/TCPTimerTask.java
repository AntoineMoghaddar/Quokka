package Network;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Rowin on 18-4-2017.
 */
public class TCPTimerTask extends TimerTask{
    private TCPHandler owner;

    private int ackNumber;

    public TCPTimerTask(TCPHandler _owner, int _ackNumber) {
        owner = _owner;
        ackNumber= _ackNumber;
    }

    @Override
    public void run() {
        if(owner.needsRetransmit(ackNumber)){
            // We need to retransmit


            //start a new Timer
            new Timer().schedule(new TCPTimerTask(owner, ackNumber),1000);
        }


    }
}
