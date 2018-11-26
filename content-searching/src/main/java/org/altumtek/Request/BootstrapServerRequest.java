package org.altumtek.Request;

import org.altumtek.networkmanager.RouteTable;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

public class BootstrapServerRequest extends BaseRequest{

    public enum BootstrapServerRequestType {
        CONNECT_REQUEST,
        LEAVE_REQUEST,
        CONNECT_RESPONSE,
        LEAVE_RESPONSE,
        ERROR
    }

    private BootstrapServerRequestType bootstrapServerRequestType;
    private String message;
    private List<RouteTable.Node> neighbourList = new ArrayList<>();


    public BootstrapServerRequest(BootstrapServerRequestType type, InetAddress myIP, int myPort, String myName) {
        if (type == BootstrapServerRequestType.CONNECT_REQUEST) {
            message = String.format("REG %s %d %s", myIP, myPort, myName);
            message = String.format("%04d", message.length() + 5) + " " + message;
        } else if (type == BootstrapServerRequestType.LEAVE_REQUEST) {
            message = String.format("UNREG %s %d %s", myIP, myPort, myName);
            message = String.format("%04d", message.length() + 5) + " " + message;
        } else {
            throw new RuntimeException("Invalid request type"); // Todo replace with custom exception
        }


    }

    public BootstrapServerRequest(String incomingMessage) {
        StringTokenizer tokens = new StringTokenizer(incomingMessage, " ");
        String length = tokens.nextToken();
        String command = tokens.nextToken();

        if (command.equals("REGOK")) {
            this.bootstrapServerRequestType = BootstrapServerRequestType.CONNECT_RESPONSE;
            int nodes = Integer.parseInt(tokens.nextToken());
            for (int i = 0; i < nodes; i++) {
                try {
                    InetAddress neighbourAddress = InetAddress.getByName(tokens.nextToken());
                    int neighbourPort = Integer.parseInt(tokens.nextToken());
                    neighbourList.add(new RouteTable.Node(true, neighbourAddress, neighbourPort));
                } catch (UnknownHostException e) {
                    e.printStackTrace();
                }
            }
        } else if(command.equals("UNROK")) {
            int value = Integer.parseInt(tokens.nextToken());
            if (value == 0) {
                this.bootstrapServerRequestType = BootstrapServerRequestType.LEAVE_RESPONSE;
            } else {
                this.bootstrapServerRequestType = BootstrapServerRequestType.ERROR;
            }
        }
    }

    public BootstrapServerRequestType getBootstrapServerRequestType() {
        return bootstrapServerRequestType;
    }

    public List<RouteTable.Node> getNeighbourList() {
        return neighbourList;
    }
}
