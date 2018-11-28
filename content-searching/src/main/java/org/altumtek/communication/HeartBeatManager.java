package org.altumtek.communication;

import org.altumtek.Request.BaseRequest;
import org.altumtek.Request.HeartbeatRequest;
import org.altumtek.networkmanager.NetworkManager;
import org.altumtek.networkmanager.RouteTable;
import org.apache.log4j.Logger;

import java.sql.Timestamp;
import java.util.*;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;

public class HeartBeatManager {

    private final static int PERIOD = 5000;
    private final static int NODE_EXPIRE = PERIOD * 4;
    private final static Logger logger = Logger.getLogger(HeartBeatManager.class);
    private final BlockingQueue<HeartbeatRequest> heartbeatQueue = new LinkedBlockingDeque<>();

    public void start() {
        sendHeartbeat();
        handleHeartbeat();
    }

    public void sendHeartbeat() {
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                for (RouteTable.Node node : NetworkManager.getInstance().getRouteTable().getNeighbourList()) {
                    BaseRequest hb = new HeartbeatRequest(NetworkManager.getInstance().getIpAddress(),
                            NetworkManager.getInstance().getPort());
                    NetworkManager.getInstance().sendMessages(hb, node.ip, node.port);
                }
            }
        }, 0, PERIOD);
    }

    private void handleHeartbeat() {
        // Remove the nodes which have not sent the last HB within the declared period
        List<RouteTable.Node> removingNodes = new ArrayList<>();
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                for (RouteTable.Node node : NetworkManager.getInstance().getRouteTable().getNeighbourList()) {
                    if ((node.getTimestamp().getTime() + NODE_EXPIRE) < System.currentTimeMillis()) {
                        removingNodes.add(node);
                    }
                }

                for (RouteTable.Node node : removingNodes) {
                    NetworkManager.getInstance().getRouteTable().removeNeighbour(node);
                }

                // Clear the removingNodes
                removingNodes.clear();
            }
        }, 0, NODE_EXPIRE);

        // Update the timestamp
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                while (true) {
                    try {
                        HeartbeatRequest hbRequest = heartbeatQueue.take();
                        Optional<RouteTable.Node> node = NetworkManager.getInstance().getRouteTable().getNeighbourList()
                                .stream()
                                .filter(n -> n.ip == hbRequest.getSenderIP())
                                .findFirst();

                        node.ifPresent(node1 -> node1.setTimestamp(new Timestamp(System.currentTimeMillis())));

                    } catch (InterruptedException e) {
                        logger.error("Exception occurred while adding HB message to the queue", e);
                    }

                    //TODO if a node is sending hb messages to you which is not in your routing table notify that particular node
                }
            }
        }, 0, PERIOD);
    }

    /**
     * Put the <code>hbMsg</code> into the {@link #queueHBMessage(HeartbeatRequest) blocking queue}
     *
     * @param hbMsg {@link HeartbeatRequest}
     * @throws InterruptedException
     */
    public void queueHBMessage(HeartbeatRequest hbMsg) {
        try {
            heartbeatQueue.put(hbMsg);

        } catch (InterruptedException e) {
            logger.error("Exception occurred while adding HB message to the queue", e);
        }
    }
}
