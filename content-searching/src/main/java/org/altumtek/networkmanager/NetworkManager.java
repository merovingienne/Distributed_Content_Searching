package org.altumtek.networkmanager;

import org.altumtek.Request.BaseRequest;

import java.util.LinkedList;

public class NetworkManager implements NetworkOperations {

    private static NetworkManager networkManager;
    private RouteTable routeTable;
    private Gossip gossip;

    private LinkedList<String> messageIds; //already received messages

    private NetworkManager () {
    }

    public static NetworkManager getInstance () {
        if (networkManager != null) {
            return networkManager;
        }
        networkManager = new NetworkManager();
        return  networkManager;
    }

    public void decodeMessage (BaseRequest request) {
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

    public void connectNetwork () {
        // Todo create network connect message send to UDP
    }

    public void leaveNetwork () {

    }

}
