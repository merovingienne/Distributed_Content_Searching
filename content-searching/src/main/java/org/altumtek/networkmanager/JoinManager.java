package org.altumtek.networkmanager;

import org.altumtek.Request.JoinRequest;
import org.apache.log4j.Logger;

import java.time.Duration;
import java.time.Instant;
import java.util.*;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;

/**
 * Created by chanuka on 11/28/18.
 */
public class JoinManager {
    private final BlockingQueue<JoinRequest> joinRequestQueue = new LinkedBlockingDeque<>();
    private HashMap<UUID, Instant> receivedList = new HashMap<>();
    private HashMap<UUID, JoinRequest> sentList = new HashMap<>();

    private static final Logger logger = Logger.getLogger(NetworkManager.class);


    public void start() {
        this.handleJoinRequest();
        this.cleanUpReceivedRequestList();
    }

    public void sendJoinRequest(RouteTable.Node node) {
        JoinRequest req = new JoinRequest();
        this.sentList.put(req.getIdentifier(), req);
        NetworkManager.getInstance().sendMessages(
                req,
                node.ip,
                node.port
        );
    }


    public void replyJoinRequest(JoinRequest incomingRequest) {
        if (receivedList.containsKey(incomingRequest.getIdentifier())) {
            return;
        }

        receivedList.put(incomingRequest.getIdentifier(), Instant.now());

        NetworkManager.getInstance().getRouteTable().addNeighbour(new RouteTable.Node(
                false,
                incomingRequest.getNewMemberIP(),
                incomingRequest.getNewMemberPort()));

        JoinRequest response = new JoinRequest(0, incomingRequest.getIdentifier());

        NetworkManager.getInstance().sendMessages(
                response,
                incomingRequest.getSenderIP(),
                incomingRequest.getSenderPort()
        );
    }

    public void handleJoinResponse(JoinRequest incomingResponse){
        if (!sentList.containsKey(incomingResponse.getSenderIdentifier())){
            return;
        }

        // we sent the message, so we should have its identifier.
        JoinRequest originalJoinRequest = sentList.get(incomingResponse.getSenderIdentifier());

        NetworkManager.getInstance().getRouteTable().addNeighbour(new RouteTable.Node(
                false,
                originalJoinRequest.getNewMemberIP(),
                originalJoinRequest.getNewMemberPort()));

        sentList.remove(originalJoinRequest.getSenderIdentifier());
    }

    public void handleJoinRequest() {
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                while (true) {
                    try {
                        JoinRequest joinRequest = joinRequestQueue.take(); // retrieve and remove
                        switch (joinRequest.getType()) {
                            case JOIN:
                                replyJoinRequest(joinRequest);
                                break;
                            case JOINOK:
                                handleJoinResponse(joinRequest);
                                break;
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }, 0, 250);
    }


    public void cleanUpReceivedRequestList() {
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                Iterator receivedListIterator = receivedList.entrySet().iterator();

                while (receivedListIterator.hasNext()) {
                    HashMap.Entry pair = (HashMap.Entry) receivedListIterator.next();
                    if (Duration.between((Instant) pair.getValue(), Instant.now()).getSeconds() > 120) {
                        receivedListIterator.remove(); // remove last returned pair
                    }
                }
            }
        }, 0, 500);
    }


    void addJoinRequestToQueue(JoinRequest req) {
        try {
            this.joinRequestQueue.put(req);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
