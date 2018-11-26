package org.altumtek.networkmanager;

import org.altumtek.Request.BootstrapServerRequest;

import java.net.InetAddress;

public class BootstrapManger {

    public void connectBootstrapServer(InetAddress bsIP, int bsPort) {
        String myName = ""; //Todo get the name from network manager

        BootstrapServerRequest bootstrapServerRequest = new BootstrapServerRequest(
                BootstrapServerRequest.BootstrapServerRequestType.CONNECT_REQUEST,
                NetworkManager.getInstance().getIpAddress(),
                NetworkManager.getInstance().getPort(),
                myName
        );

        NetworkManager.getInstance().sendMessages(
                bootstrapServerRequest,
                bsIP,
                bsPort
        );
    }

    public void handleConnectResponse(BootstrapServerRequest bootstrapServerRequest) {

        if (bootstrapServerRequest.getBootstrapServerRequestType() ==
                BootstrapServerRequest.BootstrapServerRequestType.CONNECT_RESPONSE) {

            for (RouteTable.Node node: bootstrapServerRequest.getNeighbourList()) {
                NetworkManager.getInstance().getRouteTable().addNeighbour(node);
            }

        } else if (bootstrapServerRequest.getBootstrapServerRequestType() ==
                BootstrapServerRequest.BootstrapServerRequestType.LEAVE_RESPONSE) {
            // Todo could just ignore
        }

    }

}
