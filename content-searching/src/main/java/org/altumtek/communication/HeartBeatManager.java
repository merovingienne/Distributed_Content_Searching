package org.altumtek.communication;

import org.altumtek.Request.BaseRequest;
import org.altumtek.Request.HeartbeatRequest;
import org.altumtek.networkmanager.NetworkManager;
import org.altumtek.networkmanager.RouteTable;

import java.net.DatagramSocket;
import java.util.Optional;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;

public class HeartBeatManager {

    private final static int PERIOD = 5000;
    private final static int NODE_EXPIRE = PERIOD * 4;
    private final static BlockingQueue<HeartbeatRequest> heartbeatQueue = new LinkedBlockingDeque<>();

    public void sendHeartbeat() {
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

    private static void handleHeartbeat() throws InterruptedException {
        // Remove the nodes which have not sent the last HB within the declared period
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                // FIXME this might be problematic removing elements while looping through it
                for (RouteTable.Node node : NetworkManager.getInstance().getRouteTable().getNeighbourList()) {
//                    if (node.timestamp + NODE_EXPIRE < new Timestamp(System.currentTimeMillis())) {
//                        // TODO remove the node from the table
//                    }
                }
            }
        }, 0, NODE_EXPIRE);

        // Update the timestamp
        while (true) {
            HeartbeatRequest hbRequest = heartbeatQueue.take();
            Optional<RouteTable.Node> node = NetworkManager.getInstance().getRouteTable().getNeighbourList()
                    .stream()
                    .filter(n -> n.ip == hbRequest.getSenderIP())
                    .findFirst();

            if (node.isPresent()) {
                // TODO update the timestamp of the RoutingTable.Node.timestamp
            }

            //TODO if a node is sending hb messages to you which is not in your routing table notify that particular node
        }
    }

    /**
     * Put the <code>hbMsg</code> into the {@link #queueHBMessage(HeartbeatRequest) blocking queue}
     *
     * @param hbMsg {@link HeartbeatRequest}
     * @throws InterruptedException
     */
    public static void queueHBMessage(HeartbeatRequest hbMsg) throws InterruptedException {
        heartbeatQueue.put(hbMsg);
    }
}
