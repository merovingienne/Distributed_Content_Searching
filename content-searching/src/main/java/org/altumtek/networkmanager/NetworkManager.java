package org.altumtek.networkmanager;

import org.altumtek.Request.BaseRequest;
import org.altumtek.communication.HeartBeatManager;

import java.io.IOException;
import java.net.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.StringTokenizer;

public class NetworkManager implements NetworkOperations {

    private InetAddress bootstrapServerIP;
    private int bootstrapServerPort;

    private static NetworkManager networkManager;

    private RouteTable routeTable;
    private GossipManager gossipManager;
    private HeartBeatManager heartBeatManager;
    private BootstrapManger bootstrapManger;

    private DatagramSocket networkManagerSocket;
    private InetAddress ipAddress;
    private int port;

    private List<String> messageIds; //already received messages

    private NetworkManager() {
        //Todo dynamically initialize these data
        try {
            this.bootstrapServerIP = InetAddress.getByName("127.0.0.1");
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        this.bootstrapServerPort = 55555;
    }

    public static NetworkManager getInstance() {
        if (networkManager != null) {
            return networkManager;
        }
        networkManager = new NetworkManager();
        networkManager.init();
        return networkManager;
    }

    private void init() {
        try {
            this.ipAddress = findIP();
            this.port = new Random().nextInt(10000) + 1200; // ports above 1200
            this.networkManagerSocket = new DatagramSocket(this.port);
            this.listenMessages();

            this.routeTable = new RouteTable();
            this.gossipManager = new GossipManager();
            this.heartBeatManager = new HeartBeatManager();
            this.bootstrapManger = new BootstrapManger();
            this.messageIds = new ArrayList<>();

            this.bootstrapManger.sendConnectRequest(bootstrapServerIP, bootstrapServerPort);
        } catch (Exception e) {
            e.printStackTrace();
        }
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


    public void listenMessages() {
        new Thread(() -> {
            while (true) {
                byte[] buffer = new byte[65536];
                DatagramPacket incoming = new DatagramPacket(buffer, buffer.length);
                try {
                    networkManagerSocket.receive(incoming);
                    byte[] data = incoming.getData();
                    String message = new String(data, 0, incoming.getLength());
                    echo(incoming.getAddress().getHostAddress() + " : " + incoming.getPort() + " - " + message);
                    StringTokenizer stringTokenizer = new StringTokenizer(message, " ");
                    String length = stringTokenizer.nextToken();
                    String command = stringTokenizer.nextToken();

                    if (command.equals("REGOK")) {
                        bootstrapManger.handleConnectResponse(message);
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public void connectNetwork() {
        // Todo create network connect message send to UDP
    }

    public void leaveNetwork() {

    }

    public InetAddress getIpAddress() {
        return ipAddress;
    }

    public int getPort() {
        return port;
    }

    public DatagramSocket getNetworkManagerSocket() {
        return networkManagerSocket;
    }

    // Returns the IP Address of the Node, works only for Linux and Windows
    private InetAddress findIP() throws Exception {
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

    public static void echo(String msg) {
        System.out.println(msg);
    }

}
