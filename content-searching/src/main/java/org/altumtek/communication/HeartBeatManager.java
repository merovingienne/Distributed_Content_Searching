package org.altumtek.communication;

import org.altumtek.Request.BaseRequest;
import org.altumtek.Request.HeartbeatRequest;
import org.altumtek.networkmanager.NetworkManager;
import org.altumtek.networkmanager.RouteTable;

import java.net.DatagramSocket;
import java.util.Timer;
import java.util.TimerTask;

public class HeartBeatManager {

    private final static int PERIOD = 5000;
    private final static int CHECK_FACTOR = 4;

    public void sendHeartBeat() {
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                for (RouteTable.Node node : NetworkManager.getInstance().getRouteTable().getNeighbourList()) {
                    try {
                        BaseRequest hb = new HeartbeatRequest(NetworkManager.getInstance().getIpAddress(),
                                NetworkManager.getInstance().getPort());
                        hb.serialize();
                        hb.send(node.ip, node.port, new DatagramSocket());

                    } catch (Exception e) {
                        //TODO log
                        e.printStackTrace();
                    }
                }
            }
        }, 0, PERIOD);
    }

    public void handleHeartBeat() {
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                // TODO
            }
        }, 0, (PERIOD * CHECK_FACTOR));
    }
}
