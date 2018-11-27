package org.altumtek.Request;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.StringTokenizer;

/**
 * Created by chanuka on 11/24/18.
 */
public class JoinRequest extends BaseRequest {

    InetAddress newMemberIP;
    int newMemberPort;

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

    public JoinRequest(String msg) throws UnknownHostException {
        StringTokenizer tokenizer = new StringTokenizer(msg, " ");
        String command = tokenizer.nextToken();
        if (command.equals(RequestType.JOIN.name())) {
            this.type = RequestType.JOIN;
            this.newMemberIP = InetAddress.getByName(tokenizer.nextToken());
            this.newMemberPort = Integer.parseInt(tokenizer.nextToken());
        } else if (command.equals(RequestType.JOINOK.name())) {
            int value = Integer.parseInt(tokenizer.nextToken());
            if (value == 0) {
                this.type = RequestType.JOINOK;
            } else if (value == 9999) {
                this.type = RequestType.ERROR;
            }
        }

    }

    public InetAddress getNewMemberIP() {
        return newMemberIP;
    }

    public int getNewMemberPort() {
        return newMemberPort;
    }
}
