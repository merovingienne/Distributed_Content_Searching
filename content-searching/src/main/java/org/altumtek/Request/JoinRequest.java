package org.altumtek.Request;

/**
 * Created by chanuka on 11/24/18.
 */
public class JoinRequest extends BaseRequest {

    public JoinRequest() {
        this.type = RequestType.JOIN;
        this.message.concat(serializationUtil(this.type.name()))
                .concat(serializationUtil(this.senderIP.getHostAddress()))
                .concat(serializationUtil(Integer.toString(this.senderPort)));
    }

    public JoinRequest(int value) {
        this.type = RequestType.JOINOK;
        this.message.concat(serializationUtil(this.type.name()))
                .concat(String.valueOf(value));
    }
}
