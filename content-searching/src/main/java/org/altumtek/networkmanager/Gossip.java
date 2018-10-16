package org.altumtek.networkmanager;

public class Gossip {

    private NetworkOperations networkOperations;

    Gossip (NetworkOperations networkOperations) {
        this.networkOperations = networkOperations;
    }

    public void sendGossipRequest () {
        // Todo send a gossibp request
    }

    public void resolveGossipRequest () {
        // Todo handle reply to gossip request
    }

    public void replyGossipRequest () {
        // Todo reply incoming gossip requests
    }
}
