package org.altumtek.networkmanager;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.StringTokenizer;

public class BootstrapManger {

    public void sendConnectRequest(InetAddress bsIP, int bsPort) {
        String myIP = NetworkManager.getInstance().getIpAddress().getHostAddress();
        int myPort = NetworkManager.getInstance().getPort();
        String myName = "Akila"; //Todo get the name from network manager

        String message = String.format("REG %s %d %s", myIP, myPort, myName);
        message = String.format("%04d", message.length() + 5) + " " + message;
        DatagramPacket outPacket = new DatagramPacket(message.getBytes(), message.getBytes().length,bsIP, bsPort);

        try {
            NetworkManager.getInstance().getNetworkManagerSocket().send(outPacket);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void handleConnectResponse(String message) {
        StringTokenizer tokens = new StringTokenizer(message, " ");
        String length = tokens.nextToken();
        String command = tokens.nextToken();

        if (command.equals("REGOK")) {
            int nodes = Integer.parseInt(tokens.nextToken());
            for (int i = 0; i < nodes; i++) {
                try {
                    InetAddress neighbourAddress = InetAddress.getByName(tokens.nextToken());
                    int neighbourPort = Integer.parseInt(tokens.nextToken());
                    NetworkManager.getInstance().getRouteTable().addNeighbour(new RouteTable.Node(
                            true, neighbourAddress, neighbourPort));
                } catch (UnknownHostException e) {
                    e.printStackTrace();
                }
            }
        }

    }

}
