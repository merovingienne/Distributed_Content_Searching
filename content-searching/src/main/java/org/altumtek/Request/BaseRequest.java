package org.altumtek.Request;

import org.altumtek.networkmanager.NetworkManager;
import org.apache.log4j.Logger;
import sun.rmi.runtime.Log;

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
    private final static Logger logger = Logger.getLogger(BaseRequest.class);
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

    public static BaseRequest deserialize(DatagramPacket newPacket) throws UnknownHostException {
        // TODO   Complete this.
        String packetData = new String(newPacket.getData(), 0, newPacket.getLength());
        logger.info("Receiving : "+packetData);
        packetData = packetData.replace("/","");
        String len = packetData.split(" ")[0];
        String type = packetData.split(" ")[1];
        String remainingMessage = packetData.substring(4);

        BaseRequest newRequest = null;

        switch (RequestType.valueOf(type)){
            case ACK:
                break;
            case BSC:
                break;
            case GOSSIP:
                return new GossipRequest(remainingMessage);
            case GOSSIPOK:
                return new GossipRequest(remainingMessage);
            case HEARTBEAT:
                return new HeartbeatRequest(remainingMessage);
            case JOIN:
                return new JoinRequest(remainingMessage);
            case JOINOK:
                return new JoinRequest(remainingMessage);
            case LEAVE:
                return new LeaveRequest(remainingMessage);
            case LEAVEOK:
                return new LeaveRequest(remainingMessage);
            case SER:
                return new SearchRequest(remainingMessage);
            case SEROK:
                return new SearchRequest(remainingMessage);
            case REG:
                return new BootstrapServerRequest(remainingMessage);
            case REGOK:
                return new BootstrapServerRequest(remainingMessage);
            case UNREG:
                return new BootstrapServerRequest(remainingMessage);
            case UNROK:
                return new BootstrapServerRequest(remainingMessage);
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
        newMsg = newMsg.replace("/","");
        logger.info("Sending : "+this.type.name()+" :  "+newMsg);
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
