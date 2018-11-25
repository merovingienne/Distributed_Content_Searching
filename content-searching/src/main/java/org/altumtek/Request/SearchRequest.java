package org.altumtek.Request;

import java.net.InetAddress;

/**
 * Created by chanuka on 11/24/18.
 */
public class SearchRequest extends BaseRequest {
    private final String command = "SER";

    public SearchRequest(InetAddress senderIP, int senderPort){
        this.type = RequestType.SEARCH;
        this.senderIP = senderIP;
        this.senderPort = senderPort;
    }
}
