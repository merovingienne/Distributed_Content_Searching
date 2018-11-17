package org.altumtek.networkmanager;

import org.altumtek.Request.GossipRequest;

public class GossipManager extends Thread {

    public final static int NEIGHBOUR_LIMIT = 4;

    public GossipManager() {

    }

    public void run() {
        while (true) {
            this.sendGossipRequest();
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void sendGossipRequest() {
        //Todo send gossip request
        if (NetworkManager.getInstance().getRouteTable().getNeighbourList().size() < NEIGHBOUR_LIMIT) {
            for (RouteTable.Node node: NetworkManager.getInstance().getRouteTable().getNeighbourList()) {
                GossipRequest gossipRequest = new GossipRequest();
//                    gossipRequest.send()
            }
        }
    }

    public void resolveGossipRequest() {
        // Todo handle reply to gossip request
    }

    public void replyGossipRequest() {
        // Todo reply incoming gossip requests
    }
}
