package org.altumtek.networkmanager;

import org.altumtek.Request.*;

import java.net.InetAddress;

public class BootstrapManger {

    public void connectBootstrapServer(InetAddress bsIP, int bsPort) {
        BootstrapServerRequest bootstrapServerRequest = new BootstrapServerRequest(
                RequestType.REG
        );
        NetworkManager.getInstance().sendMessages(
                bootstrapServerRequest,
                bsIP,
                bsPort
        );
    }

    public void disconnectBootstrapServer(InetAddress bsIP, int bsPort) {
        BootstrapServerRequest bootstrapServerRequest = new BootstrapServerRequest(
                RequestType.UNREG
        );

        NetworkManager.getInstance().sendMessages(
                bootstrapServerRequest,
                bsIP,
                bsPort
        );
    }

    public void joinNetwork() {
//        JoinRequest joinRequest = new JoinRequest();
        for (RouteTable.Node node: NetworkManager.getInstance().getRouteTable().getNeighbourList()){
            NetworkManager.getInstance().getJoinManager().sendJoinRequest(node);
        }
    }

    public void leaveNetwork() {
        LeaveRequest leaveRequest = new LeaveRequest();
        for (RouteTable.Node node: NetworkManager.getInstance().getRouteTable().getNeighbourList()){
            NetworkManager.getInstance().sendMessages(leaveRequest, node.ip, node.port);
        }
    }

    public void handleConnectResponse(BaseRequest request) {

        if (request.getType() ==
                RequestType.REGOK) {
            BootstrapServerRequest bootstrapServerRequest = (BootstrapServerRequest)request;
            for (RouteTable.Node node: bootstrapServerRequest.getNeighbourList()) {
                NetworkManager.getInstance().getRouteTable().addNeighbour(node);
            }

            this.joinNetwork();

        } else if (request.getType() ==
                RequestType.UNROK) {
            this.leaveNetwork();
        } else if(request.getType() == RequestType.LEAVE) {
            LeaveRequest leaveRequest = (LeaveRequest) request;
            NetworkManager.getInstance().getRouteTable().removeNeighbour(new RouteTable.Node(
                    false,
                    leaveRequest.getLeavingMemberIP(),
                    leaveRequest.getLeavingMemberPort()));

        } else if(request.getType() == RequestType.LEAVEOK) {

        }

    }



}
