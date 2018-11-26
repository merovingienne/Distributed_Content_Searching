package org.altumtek.Request;

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

    protected UUID identifier;
    protected RequestType type;
    protected InetAddress senderIP;
    protected int senderPort;
    protected int hopCount;
    protected DatagramPacket packet;
    protected HashMap<String, String> data = new HashMap<>();
    protected boolean dataReady = false;

    private static final String STR_ID = "ID";
    private static final String STR_TYPE = "TYPE";
    private static final String STR_SENDER_IP = "SENDER_IP";
    private static final String STR_SENDER_PORT = "SENDER_PORT";
    private static final String STR_HOP_COUNT = "HOP_COUNT";

    BaseRequest() {
        this.identifier = UUID.randomUUID();
    }


    public void serialize() {
        // create packet data
        String serializedData = " "; // space between msg length and rest of msg

        // NON-FINAL
        // Order needs to change depending on the request type.
        // May need discussion.
        String _ID = serializationUtil(STR_ID, true, identifier.toString());
        String _TYPE = serializationUtil(STR_TYPE, true, type.name());
        String _SENDER_IP = serializationUtil(STR_SENDER_IP, false, senderIP.getHostAddress());
        String _SENDER_PORT = serializationUtil(STR_SENDER_PORT, false, Integer.toString(senderPort));
        String _HOP_COUNT = serializationUtil(STR_HOP_COUNT, false, Integer.toString(hopCount));

        switch (RequestType.valueOf(type.name())){
            case BSC:
//                Handled by BootstrapServerRequest class
//                serializedData.concat(getCommand())
//                .concat(_SENDER_IP)
//                .concat(_SENDER_PORT);
                break;
            // Below are messages between peers
            case GOSSIP:
                // handled internally by GossipRequest class
                break;
            case HEARTBEAT:
                break;
            case ACK:
            case JOIN:
            case LEAVE:
                serializedData.concat(getCommand())
                        .concat(_SENDER_IP)
                        .concat(_SENDER_PORT);
                break;
        }

        // Serialize custom data as "key:value"
        for (String key : data.keySet()) {
            String keyVal = serializationUtil(key, true, data.get(key));
            if ((serializedData.getBytes().length + keyVal.getBytes().length) < 254) {
                serializedData.concat(keyVal);
            }
        }

        // set message length at the beginning
        int length = serializedData.length() + 4;
        serializedData = String.format("%04d", length) + serializedData; // we have added first space between length and param already


        // Ready packet
        this.packet = new DatagramPacket(serializedData.getBytes(), 0, serializedData.getBytes().length);
        this.dataReady = true;
    }

    /**
     * Overloaded serialization method for Search requests
     *
     * @param fileName - file to be searched
     */
    public void serialize(String fileName) {
        // create packet data
        String serializedData = " "; // space between msg length and rest of msg

        String _ID = serializationUtil(STR_ID, true, identifier.toString());
        String _TYPE = serializationUtil(STR_TYPE, true, type.name());
        String _SENDER_IP = serializationUtil(STR_SENDER_IP, false, senderIP.getHostAddress());
        String _SENDER_PORT = serializationUtil(STR_SENDER_PORT, false, Integer.toString(senderPort));
        String _HOP_COUNT = serializationUtil(STR_HOP_COUNT, false, Integer.toString(hopCount));

        serializedData.concat(getCommand())
                .concat(_SENDER_IP)
                .concat(_SENDER_PORT)
                .concat(fileName)
                .concat(_HOP_COUNT)
                .concat(_ID)
                .concat(_TYPE);

        // Serialize custom data as "key:value"
        for (String key : data.keySet()) {
            String keyVal = serializationUtil(key, true, data.get(key));
            if ((serializedData.getBytes().length + keyVal.getBytes().length) < 254) {
                serializedData.concat(keyVal);
            }
        }

        // set message length at the beginning
        int length = serializedData.length() + 4;
        serializedData = String.format("%04d", length) + serializedData; // we have added first space between length and param already


        // Ready packet
        this.packet = new DatagramPacket(serializedData.getBytes(), 0, serializedData.getBytes().length);
        this.dataReady = true;
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
        HashMap<String, String> data = deserializationUtil(packetData);

        BaseRequest newRequest = new DummyRequest();

        switch (RequestType.valueOf(data.get(STR_TYPE))){
            case ACK:
//                newRequest = new AcknowledgementRequest();
                break;
            case BSC:
                break;
            case GOSSIP:
//                newRequest = new GossipRequest();
                break;
            case HEARTBEAT:
//                newRequest = new HeartbeatRequest();
                break;
            case SEARCH:
//                newRequest = new SearchRequest();
                break;
        }

        newRequest.identifier = UUID.fromString(data.get(STR_ID));
        try {
            newRequest.senderIP = InetAddress.getByName(data.get(STR_SENDER_IP));
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        newRequest.senderPort = Integer.decode(data.get(STR_SENDER_PORT));
        newRequest.hopCount = Integer.decode(data.get(STR_HOP_COUNT));
        newRequest.data = data;

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
     * Util method to deserialize data from request.
     *
     * @param dataString
     * @return
     */
    private static HashMap<String, String> deserializationUtil(String dataString){
        HashMap<String, String> data = new HashMap<String, String>();

        String localType = RequestType.DUMMY.name();
        ArrayList<String> orderedDataWithoutKeys = new ArrayList();

        String[] dataList = dataString.split(" ");

        // TODO Complete and clean up date extraction.

        for (String component : dataList) {
            System.out.println("Decoded component: " + component);
            String[] temp = component.split(":");

            if (temp.length > 1){
                for (String k : temp){
                    System.out.println("decoded: " + k);
                }
                if (temp[0].equalsIgnoreCase(STR_TYPE)){
                    localType = temp[1];
                }
                data.put(temp[0], temp[1]);
            } else {
                orderedDataWithoutKeys.add(temp[0]);
            }
        }

        // TODO extract data for each request type.

        switch (RequestType.valueOf(localType)){
            case ACK:
//                data.put();
                break;
            case BSC:
                break;
            case GOSSIP:
                break;
            case HEARTBEAT:
                break;
            case SEARCH:
//                newRequest = new SearchRequest();
                break;
        }

        return data;
    }

    /**
     * Get UUID for comparison purposes.
     *
     * @return
     */
    public String getID() {
        return this.identifier.toString();
    }

    /**
     * To read STR_ID from incoming request (UDP)
     * and set in recreated request.
     * @param ID
     */
    public void setCustomID(UUID ID){
        this.identifier = ID;
    }

    public UUID getIdentifier() {
        return identifier;
    }

    public void setHopCount(int count){
        this.hopCount = count;
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

    public String getCommand(){
        // handle in relevant derived classes : BSServerCommand, JOIN, LEAVE
        return "ERROR : Base command.";
    }

    public String getResponse(){
        // handle in relevant derived classes : BSServerCommand, JOIN, LEAVE
        return "ERROR : Base Response.";
    }
}
