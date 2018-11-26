package org.altumtek.Request;

import java.net.InetAddress;

/**
 * Created by chanuka on 11/24/18.
 */
public class JoinRequest extends BaseRequest {
    private final String command = "JOIN";
    private final String response = "JOINOK";

    public JoinRequest(InetAddress senderIP, int senderPort){
        this.type = RequestType.JOIN;
        this.senderIP = senderIP;
        this.senderPort = senderPort;
    }

    @Override
    public String getCommand(){
        return command;
    }
}
