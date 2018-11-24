package org.altumtek.Request;

import org.altumtek.networkmanager.RouteTable;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
import java.util.stream.Collectors;

/**
 * Gossip Request
 * Sent between nodes to discover peers.
 * <p>
 * Created by chanuka on 10/14/18.
 */
public class GossipRequest extends BaseRequest {

    public enum GossipRequestType {
        REQUEST,
        RESPONSE,
    }

    private GossipRequestType gossipType;
    private List<RouteTable.Node> neighbourList = new ArrayList<>();

    public GossipRequest(GossipRequestType gossipType, List<RouteTable.Node> nodeList) {
        this.type = RequestType.GOSSIP;
        this.gossipType = gossipType;

        String msg = nodeList
                .stream()
                .map(node->node.ip + "-" + node.port)
                .collect(Collectors.joining("-"));

        switch (gossipType) {
            case REQUEST:
                msg = "REQ-"+msg;
                break;
            case RESPONSE:
                msg = "RES-"+msg;
                break;
        }

        super.data.put("msg", msg);
    }

    public GossipRequest(String msg) throws UnknownHostException{
        StringTokenizer tokenizer = new StringTokenizer(msg, "-");
        String gossipType = tokenizer.nextToken();

        if (gossipType.equals("REQ")) {
            this.gossipType= GossipRequestType.REQUEST;
        } else if (gossipType.equals("RES")) {
            this.gossipType = GossipRequestType.RESPONSE;
        }

        while (tokenizer.hasMoreElements()) {
            InetAddress ip = InetAddress.getByName(tokenizer.nextToken());
            int port = Integer.valueOf(tokenizer.nextToken());
            neighbourList.add(new RouteTable.Node(false, ip, port));
        }
    }

    public List<RouteTable.Node> getNeighbourList() {
        return neighbourList;
    }

    public GossipRequestType getGossipType() {
        return gossipType;
    }
}
