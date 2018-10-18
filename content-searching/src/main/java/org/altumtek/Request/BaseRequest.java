package org.altumtek.Request;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
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

    protected UUID identifier;
    protected RequestType type;
    protected InetAddress senderIP;
    protected int senderPort;
    protected int hopCount;
    protected DatagramPacket packet;
    protected HashMap<String, String> data;
    protected boolean dataReady = false;

    BaseRequest() {
        this.identifier = UUID.randomUUID();
    }


    public void serialize() {
        // create packet data
        String serializedData = " "; // space between msg length and rest of msg

        // NON-FINAL
        // Order needs to change depending on the request type.
        // May need discussion.
        serializedData.concat(serializationUtil("ID", true, identifier.toString()))
                .concat(serializationUtil("type", true, type.name()))
                .concat(serializationUtil("senderIP", false, senderIP.getHostAddress()))
                .concat(serializationUtil("senderPort", false, Integer.toString(senderPort)))
                .concat(serializationUtil("hopCount", false, Integer.toString(hopCount)));


        // Serialize custom data as "key:value"
        for (String key : data.keySet()) {
            String keyVal = serializationUtil(key, true, data.get(key));
            if ((serializedData.getBytes().length + keyVal.getBytes().length) < 254) {
                serializedData.concat(keyVal);
            }
        }

        // set message length at the beginning
        int length = serializedData.length() + 4;
        serializedData = String.format("%04d", length) + serializedData;


        // Ready packet
        this.packet = new DatagramPacket(serializedData.getBytes(), 0, serializedData.getBytes().length);
        this.dataReady = true;
    }

    public void deserialize(DatagramPacket newPacket) {
        // TODO   Recreate a request from new incoming packet
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
    public void send(InetAddress IP, int port, DatagramSocket socket) throws Exception {
        if (dataReady) {
            this.packet.setAddress(IP);
            this.packet.setPort(port);
            socket.send(this.packet);
        } else {
            throw new DataNotReadyException();
        }
    }

    /**
     * Forward a request to another node
     * after increasing hop count by one.
     *
     * @param IP
     * @param port
     * @param socket
     * @throws Exception
     */
    public void forward(InetAddress IP, int port, DatagramSocket socket) throws Exception {
        this.increaseHopCount();
        this.send(IP, port, socket);
    }

    /**
     * Increase hop count when going through
     * an intermediary node.
     */
    public void increaseHopCount() {
        this.hopCount += 1;
    }

    /**
     * Util method to create serialized data string components.
     * <p>
     * Custom data follows the format "key:value".
     *
     * @param key
     * @param includeKey
     * @param value
     * @return
     */
    private String serializationUtil(String key, boolean includeKey, String value) {
        if (includeKey) {
            return " " + key + ":" + value;
        }
        return " " + value;
    }

    /**
     * Get UUID for comparison purposes.
     *
     * @return
     */
    public String getID() {
        return this.identifier.toString();
    }

}
