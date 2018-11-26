package org.altumtek.Request;

import org.altumtek.networkmanager.NetworkManager;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

/**
 * BaseRequest
 * Base class for different request types.
 * Wraps UDP DatagramPacket class.
 * <p>
 * Created by chanuka on 10/14/18.
 */
public abstract class BaseRequest {

    protected RequestType type;
    protected InetAddress senderIP;
    protected int senderPort;
    protected String message = "";

    public BaseRequest(){
        this.senderIP = NetworkManager.getInstance().getIpAddress();
        this.senderPort = NetworkManager.getInstance().getPort();
    }

    /**
     * Recreate a request from new incoming packet
     *
     * @param newPacket
     * @return
     */

    public static BaseRequest deserialize(DatagramPacket newPacket){
        // TODO   Complete this.
        String packetData = new String(newPacket.getData(), 0, newPacket.getLength());
        String len = packetData.split(" ")[0];
        String type = packetData.split(" ")[1];
        String remainingMessage = ""; // TODO Complete this.

        BaseRequest newRequest = new DummyRequest();

        switch (RequestType.valueOf(type)){
            case ACK:
//                newRequest = new AcknowledgementRequest();
                break;
            case BSC:
                break;
            case GOSSIP:
//                newRequest = new GossipRequest(remainingMessage);
//                newRequest = new GossipRequest();
                break;
            case HEARTBEAT:
//                newRequest = new HeartbeatRequest();
                break;
            case SER:
//                newRequest = new SearchRequest();
                break;
        }

        return newRequest;
    }

    /**
     * Send this request to the specified IP and port
     * via the given UDP socket.
     *
     * @param IP
     * @param port
     * @param socket
     * @throws Exception
     */
    public void send(InetAddress IP, int port, DatagramSocket socket) throws IOException {
        String newMsg = setLength(this.message);
        DatagramPacket packet = new DatagramPacket(newMsg.getBytes(), 0, newMsg.getBytes().length);
        packet.setAddress(IP);
        packet.setPort(port);
        try {
            socket.send(packet);
        } catch (IOException e){
            e.printStackTrace();
            throw new IOException(e);
        }
    }

    /**
     * Util method to create serialized data string components.
     * <p>
     * Custom data follows the format "key:value".
     *
     * @param value
     * @return
     */

    public String serializationUtil(String value) {
        return " " + value;
    }

    public RequestType getType() {
        return type;
    }

    public InetAddress getSenderIP() {
        return senderIP;
    }

    public int getSenderPort() {
        return senderPort;
    }

    public String setLength(String msg){
        return String.format("%04d", msg.length() + 4) + msg; // we have added first space between length and param already
    }
}
