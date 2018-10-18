package org.altumtek.networkmanager;

import java.util.LinkedList;
import java.util.List;

public class RouteTable {

    private final List<Node> neighbourList = new LinkedList();

    public void addNeighbour(Node d) {
        neighbourList.add(d);
    }

    public List<Node> getNeighbourList() {
        return neighbourList;
    }

    public static class Node {

        public boolean primary;
        public String ip;
        public int port;

        public Node(boolean primary, String ip, int port) {
            this.primary = primary;
            this.ip = ip;
            this.port = port;
        }
    }

}
