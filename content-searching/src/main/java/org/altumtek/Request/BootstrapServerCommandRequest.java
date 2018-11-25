package org.altumtek.Request;

import java.net.InetAddress;

/**
 * Created by chanuka on 11/24/18.
 */
public class BootstrapServerCommandRequest extends BaseRequest {
    private String command;

    public BootstrapServerCommandRequest(String command, InetAddress senderIP, int senderPort) throws InvalidInputException {
        this.type = RequestType.BSC;
        this.senderIP = senderIP;
        this.senderPort = senderPort;

        if (command.equalsIgnoreCase("REG") || command.equalsIgnoreCase("UNREG")){
            this.command = command.toUpperCase();
        } else {
            throw new InvalidInputException();
        }
    }

    @Override
    public String getCommand(){
        return command;
    }
}
