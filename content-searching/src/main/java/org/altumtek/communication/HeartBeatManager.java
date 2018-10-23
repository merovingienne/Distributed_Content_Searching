package org.altumtek.communication;

import org.altumtek.networkmanager.NetworkManager;
import org.altumtek.networkmanager.RouteTable;

import java.util.Timer;
import java.util.TimerTask;

public class HeartBeatManager {

    private final static int PERIOD = 5000;

    public void sendHeartBeat() {
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                for (RouteTable.Node node : NetworkManager.getInstance().getRouteTable().getNeighbourList()) {
                    new HeartBeat(node.ip, node.port).start();
                }
            }
        }, 0, PERIOD);
    }
}
