package org.altumtek.Request;

import java.net.InetAddress;

/**
 * Created by chanuka on 11/24/18.
 */
public class LeaveRequest extends BaseRequest {
    private final String command = "LEAVE";
    private final String response = "LEAVEOK";

    public LeaveRequest(InetAddress senderIP, int senderPort){
        this.type = RequestType.LEAVE;
        this.senderIP = senderIP;
        this.senderPort = senderPort;
    }

    @Override
    public String getCommand(){
        return command;
    }
}
