package org.altumtek.networkmanager;

import org.altumtek.Request.GossipRequest;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.stream.Collectors;

public class GossipManager {

    private final static int NEIGHBOUR_LIMIT = 4;
    private final static int GOSSIP_PERIOD = 1000;
    private final static BlockingQueue<GossipRequest> gossipRequestQueue = new LinkedBlockingDeque<>();

    public void start () {
        this.handleGossipRequests();
        this.sendGossipRequests();
    }

    private void processGossipReply(GossipRequest gossipRequest) {
        List<RouteTable.Node> newNodeList = gossipRequest.getNeighbourList().stream()
                .filter(neighbour -> NetworkManager.getInstance().getRouteTable().containsNode(neighbour.ip))
                .collect(Collectors.toList());

        for (RouteTable.Node node: newNodeList) {
            NetworkManager.getInstance().getRouteTable().addNeighbour(node);
        }

    }

    private void replyGossipRequest(GossipRequest gossipRequest) {
        processGossipReply(gossipRequest); //to add nodes in gossip request to route table
        List<RouteTable.Node> nodesList = new ArrayList<>(NetworkManager.getInstance().getRouteTable()
                .getNeighbourList());
        GossipRequest replyGossipRequest = new GossipRequest(GossipRequest.GossipRequestType.REQUEST,
                nodesList);
        NetworkManager.getInstance().sendMessages(replyGossipRequest,
                gossipRequest.getSenderIP(),gossipRequest.getSenderPort());
    }


   public void handleGossipRequests() {
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                while (gossipRequestQueue.size() > 0) {
                    try {
                        GossipRequest gossipRequest = gossipRequestQueue.take();
                        if (gossipRequest.getGossipType() == GossipRequest.GossipRequestType.RESPONSE) {
                            processGossipReply(gossipRequest);
                        } else if(gossipRequest.getGossipType() == GossipRequest.GossipRequestType.REQUEST) {
                            replyGossipRequest(gossipRequest);
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }, 0, GOSSIP_PERIOD);
   }

    public void sendGossipRequests() {
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                if (NetworkManager.getInstance().getRouteTable().getNeighbourList().size() < NEIGHBOUR_LIMIT) {
                    List<RouteTable.Node> nodesList = new ArrayList<>(NetworkManager.getInstance().getRouteTable()
                            .getNeighbourList());
                    for (RouteTable.Node node: NetworkManager.getInstance().getRouteTable().getNeighbourList()) {
                        GossipRequest gossipRequest = new GossipRequest(GossipRequest.GossipRequestType.REQUEST,
                                nodesList);
                        NetworkManager.getInstance().sendMessages(gossipRequest, node.ip, node.port);
                    }
                }
            }
        },0,GOSSIP_PERIOD);
    }

    public void addGossipRequest (GossipRequest gossipRequest) {
        try {
            gossipRequestQueue.put(gossipRequest);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
