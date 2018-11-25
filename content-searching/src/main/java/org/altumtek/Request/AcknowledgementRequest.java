package org.altumtek.Request;

import java.net.InetAddress;

/**
 * Acknowledgement of UDP packet receipt.
 * Used to implement reliable communication.
 * <p>
 * Created by chanuka on 10/14/18.
 */
public class AcknowledgementRequest extends BaseRequest {

    public AcknowledgementRequest(InetAddress senderIP, int senderPort) {
        this.type = RequestType.ACK;
        this.senderIP = senderIP;
        this.senderPort = senderPort;
    }
}
