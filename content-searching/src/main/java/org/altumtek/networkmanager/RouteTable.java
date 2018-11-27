package org.altumtek.networkmanager;

import java.net.InetAddress;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class RouteTable {

    private final List<Node> neighbourList = Collections.synchronizedList(new ArrayList<>());

    public void addNeighbour(Node node) {
        int removeIndex = -1;
        for (int i = 0; i < neighbourList.size(); i++) {
            if (neighbourList.get(i).ip == node.ip && neighbourList.get(i).port == node.port) {
                removeIndex = i;
                break;
            }
        }
        if (removeIndex!= -1) {
            neighbourList.remove(removeIndex);
        }
        neighbourList.add(node);
    }

    public void removeNeighbour(Node node) {
        int removeIndex = -1;
        for (int i = 0; i < neighbourList.size(); i++) {
            if (neighbourList.get(i).ip == node.ip && neighbourList.get(i).port == node.port) {
                removeIndex = i;
                break;
            }
        }
        if (removeIndex!= -1) {
            neighbourList.remove(removeIndex);
        }
    }

    public List<Node> getNeighbourList() {
        return neighbourList;
    }

    public boolean containsNode(InetAddress inetAddress) {
        for (Node d : neighbourList) {
            if (d.ip.equals(inetAddress)) return true;
        }
        return false;
    }

    public static class Node {

        public boolean primary;
        public InetAddress ip;
        public int port;
        public Timestamp timestamp;

        public Node(boolean primary, InetAddress ip, int port, Timestamp timestamp) {
            this.primary = primary;
            this.ip = ip;
            this.port = port;
            this.timestamp = timestamp;
        }

        public Node(boolean primary, InetAddress ip, int port) {
            this.primary = primary;
            this.ip = ip;
            this.port = port;
        }

        public boolean isPrimary() {
            return primary;
        }

        public void setPrimary(boolean primary) {
            this.primary = primary;
        }

        public InetAddress getIp() {
            return ip;
        }

        public void setIp(InetAddress ip) {
            this.ip = ip;
        }

        public int getPort() {
            return port;
        }

        public void setPort(int port) {
            this.port = port;
        }

        public Timestamp getTimestamp() {
            return timestamp;
        }

        public void setTimestamp(Timestamp timestamp) {
            this.timestamp = timestamp;
        }
    }

}
