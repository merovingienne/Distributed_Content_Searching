package org.altumtek.networkmanager;

import org.altumtek.Request.BaseRequest;

import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.LinkedList;

public class NetworkManager implements NetworkOperations {

    private static NetworkManager networkManager;
    private RouteTable routeTable = new RouteTable();
    private Gossip gossip;

    private LinkedList<String> messageIds; //already received messages

    private NetworkManager() {
    }

    public static NetworkManager getInstance() {
        if (networkManager != null) {
            return networkManager;
        }
        networkManager = new NetworkManager();
        return networkManager;
    }

    public void decodeMessage(BaseRequest request) {
        if (messageIds.contains(request.getID())) {
            // TODO message has already reecived
            return;
        }
        // TODO implement this
//        RequestType type = null;
//        switch (){
//            case ():
//                break;
//        }
    }

    public void encodeMessage(BaseRequest request) {

    }

    public RouteTable getRouteTable() {
        return routeTable;
    }

    public void connectNetwork() {
        // Todo create network connect message send to UDP
    }

    public void leaveNetwork() {

    }

    /**
     * Returns the {@link InetAddress IP Address} of the Node.
     * <p>
     * Works only for Linux and Windows
     *
     * @return the IP address of the node
     * @throws Exception
     */
    public InetAddress getIP() throws Exception {
        try (final DatagramSocket socket = new DatagramSocket()) {
            socket.connect(InetAddress.getByName("8.8.8.8"), 10002);
            return socket.getInetAddress();

        } catch (SocketException e) {
            e.printStackTrace();
            //TODO log
            throw new Exception("socket exception");

        } catch (UnknownHostException e) {
            e.printStackTrace();
            //TODO log
            throw new Exception("Unknown host exception");
        }
    }

}
