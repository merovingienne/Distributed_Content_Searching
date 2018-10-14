package org.altumtek.Request;

/**
 * Gossip Request
 * Sent between nodes to discover peers.
 *
 * Created by chanuka on 10/14/18.
 */
public class GossipRequest extends BaseRequest{

    public GossipRequest(){
        this.type = RequestType.GOSSIP;
    }
}
