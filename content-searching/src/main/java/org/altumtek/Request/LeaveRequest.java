package org.altumtek.Request;

/**
 * Created by chanuka on 11/24/18.
 */
public class LeaveRequest extends BaseRequest {

    public LeaveRequest() {
        this.type = RequestType.LEAVE;
        this.message.concat(serializationUtil(this.type.name()))
                .concat(serializationUtil(this.senderIP.getHostAddress()))
                .concat(serializationUtil(Integer.toString(this.senderPort)));
    }

    public LeaveRequest(int value) {
        this.type = RequestType.LEAVEOK;
        this.message.concat(serializationUtil(this.type.name()))
                .concat(String.valueOf(value));
    }
}
