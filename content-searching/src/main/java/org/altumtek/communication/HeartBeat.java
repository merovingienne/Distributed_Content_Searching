package org.altumtek.communication;

import org.altumtek.Request.BaseRequest;
import org.altumtek.Request.HeartbeatRequest;
import org.altumtek.networkmanager.NetworkManager;
import org.altumtek.networkmanager.RouteTable;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Sends a Heart Beat to the neighbours
 */
public class HeartBeat extends TimerTask {

//    private RouteTable routeTable; FIXME don't get the routing table like this because it will be keep updating (because of gossiping)

    private final static int PERIOD = 5000;

    public void sendHeartBeat() {
        for (RouteTable.Node node : NetworkManager.getInstance().getRouteTable().getNeighbourList()) {
            new Thread(() -> {
                try {
                    BaseRequest hb = new HeartbeatRequest();
                    hb.serialize();
//                        hb.send(node.ip, node.port, new DatagramSocket()); todo

                } catch (Exception e) {
                    //TODO log
                    e.printStackTrace();
                }
            }).start();
        }
    }

    public void run() {
        sendHeartBeat();
    }

    public static void main(String[] args) {
        Timer t = new Timer();
        //schedule instead of scheduleAtFixedRate because it is ok getting somewhat delayed due to GC
        t.schedule(new HeartBeat(), 0, PERIOD);

    }
}
