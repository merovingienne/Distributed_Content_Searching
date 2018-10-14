package org.altumtek.Request;

/**
 * Heartbeat Request
 * Sent to a node's peers to announce health status.
 *
 * Created by chanuka on 10/14/18.
 */
public class HeartbeatRequest extends BaseRequest {

    public HeartbeatRequest(){
        this.type = RequestType.HEARTBEAT;
    }
}
