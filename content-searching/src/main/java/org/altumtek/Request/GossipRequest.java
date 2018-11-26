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

    private List<RouteTable.Node> neighbourList = new ArrayList<>();

    /**
     * Constructor to create outbound Gossip request.
     *
     * @param nodeList
     */
    public GossipRequest(List<RouteTable.Node> nodeList) {
        this.type = RequestType.GOSSIP;

        String nodeMsg = nodeList
                .stream()
                .map(node -> node.ip + " " + node.port)
                .collect(Collectors.joining(" "));

        this.message.concat(serializationUtil(this.type.name()))
                .concat(serializationUtil(this.senderIP.getHostAddress()))
                .concat(serializationUtil(Integer.toString(this.senderPort)))
                .concat(serializationUtil(nodeMsg));

    }

    /**
     * Constructor to recreate Gossip request from incoming request.
     *
     * @param msg
     * @throws UnknownHostException
     */
    public GossipRequest(String msg) throws UnknownHostException {
        StringTokenizer tokenizer = new StringTokenizer(msg, "0");

        tokenizer.nextToken(); // request type

        this.senderIP = InetAddress.getByName(tokenizer.nextToken());
        this.senderPort = Integer.parseInt(tokenizer.nextToken());

        while (tokenizer.hasMoreElements()) {
            InetAddress ip = InetAddress.getByName(tokenizer.nextToken());
            int port = Integer.valueOf(tokenizer.nextToken());
            neighbourList.add(new RouteTable.Node(false, ip, port));
        }
    }

    public List<RouteTable.Node> getNeighbourList() {
        return neighbourList;
    }

}
