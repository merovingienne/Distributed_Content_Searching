package org.altumtek.Request;

import java.net.InetAddress;

/**
 * Gossip Request
 * Sent between nodes to discover peers.
 * <p>
 * Created by chanuka on 10/14/18.
 */
public class GossipRequest extends BaseRequest {

    public GossipRequest(InetAddress senderIP, int senderPort) {
        this.type = RequestType.GOSSIP;
        this.senderIP = senderIP;
        this.senderPort = senderPort;
    }
}
