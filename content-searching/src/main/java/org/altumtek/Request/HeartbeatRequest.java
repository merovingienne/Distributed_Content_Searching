package org.altumtek.Request;

import java.net.InetAddress;

/**
 * Heartbeat Request
 * Sent to a node's peers to announce health status.
 * <p>
 * Created by chanuka on 10/14/18.
 */
public class HeartbeatRequest extends BaseRequest {

    public HeartbeatRequest(InetAddress senderIP, int senderPort) {
        this.type = RequestType.HEARTBEAT;
        this.senderIP = senderIP;
        this.senderPort = senderPort;

        this.message.concat(serializationUtil(this.type.name()))
                .concat(serializationUtil(this.senderIP.getHostAddress()))
                .concat(serializationUtil(Integer.toString(this.senderPort)));
    }
}
