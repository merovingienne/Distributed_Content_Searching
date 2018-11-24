package org.altumtek.networkmanager;

import org.altumtek.Request.BaseRequest;
import org.altumtek.Request.BootstrapServerRequest;
import org.altumtek.Request.GossipRequest;
import org.altumtek.Request.HeartbeatRequest;
import org.altumtek.communication.HeartBeatManager;

import java.io.IOException;
import java.net.*;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class NetworkManager {

    private static final String BOOTSTRAP_SERVER_IP_STR = "127.0.0.1";
    private static final int BOOTSTRAP_SERVER_PORT = 55555;

    private final InetAddress BOOTSTRAP_SERVER_IP;
    private final InetAddress IP_ADDRESS;
    private final int PORT;
    private final DatagramSocket networkManagerSocket;

    private static NetworkManager networkManager;
    private RouteTable routeTable;
    private GossipManager gossipManager;
    private HeartBeatManager heartBeatManager;
    private BootstrapManger bootstrapManger;

    private Map<String, BaseRequest> sendMessages; //send messages
    private Map<String, BaseRequest> receiveMessages; //send messages


    private NetworkManager() throws UnknownHostException, SocketException {
        this.BOOTSTRAP_SERVER_IP = InetAddress.getByName(BOOTSTRAP_SERVER_IP_STR);
        this.IP_ADDRESS = findIP();
        this.PORT = new Random().nextInt(10000) + 1200; // ports above 1200

        this.networkManagerSocket = new DatagramSocket(this.PORT);
        this.sendMessages = new ConcurrentHashMap<>();
        this.receiveMessages = new ConcurrentHashMap<>();
    }

    public static NetworkManager getInstance() {
        if (networkManager != null) return networkManager;

        try {
            networkManager = new NetworkManager();
            networkManager.init();
        } catch (UnknownHostException | SocketException e) {
            e.printStackTrace();
        }
        return networkManager;
    }

    private void init() throws SocketException {

        this.routeTable = new RouteTable();

        this.listenMessages();

        this.gossipManager = new GossipManager();
        this.gossipManager.start();

        this.heartBeatManager = new HeartBeatManager();
        //Fixme this.heartBeatManager.start()

        this.bootstrapManger = new BootstrapManger();
        this.bootstrapManger.connectBootstrapServer(BOOTSTRAP_SERVER_IP, BOOTSTRAP_SERVER_PORT);

    }

    private void listenMessages() {
        new Thread(() -> {
            while (true) {
                byte[] buffer = new byte[65536];
                DatagramPacket incoming = new DatagramPacket(buffer, buffer.length);
                try {
                    networkManagerSocket.receive(incoming);
                    //Todo call Chanuka's static method
                    BaseRequest request = null;//BaseRequest.

                    if (receiveMessages.containsKey(request.getID()))
                        continue;

                    receiveMessages.put(request.getID(), request);

                    if (request instanceof GossipRequest) {
                        gossipManager.addGossipRequest((GossipRequest) request);
                    } else if (request instanceof HeartbeatRequest) {
                        try {
                            HeartBeatManager.queueHBMessage((HeartbeatRequest) request);
                        } catch (InterruptedException e) {
                            e.printStackTrace(); // Todo handle within Manager if possible
                        }
                    } else if (request instanceof BootstrapServerRequest) {
                        bootstrapManger.handleConnectResponse((BootstrapServerRequest) request);
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public void sendMessages (BaseRequest request, InetAddress ip, int port) {
        try {
            request.send(ip,port,networkManagerSocket);
            sendMessages.put(request.getID(), request);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public InetAddress getIpAddress() {
        return IP_ADDRESS;
    }

    public int getPort() {
        return PORT;
    }

    public RouteTable getRouteTable() {
        return routeTable;
    }

    // Returns the IP Address of the Node, works only for Linux and Windows
    private InetAddress findIP() throws SocketException{
        try (final DatagramSocket socket = new DatagramSocket()) {
            socket.connect(InetAddress.getByName("8.8.8.8"), 10002);
            return socket.getInetAddress();

        } catch (SocketException e) {
            e.printStackTrace();
            //TODO log
            throw new SocketException("socket exception");

        } catch (UnknownHostException e) {
            e.printStackTrace();
            //TODO log
            throw new SocketException("Unknown host exception");
        }
    }

}
