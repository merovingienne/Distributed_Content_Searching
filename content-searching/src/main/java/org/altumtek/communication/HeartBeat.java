package org.altumtek.communication;

import org.altumtek.Request.BaseRequest;
import org.altumtek.Request.HeartbeatRequest;
import org.altumtek.networkmanager.NetworkManager;

import java.net.DatagramSocket;
import java.net.InetAddress;

/**
 * Sends a Heart Beat to the specified neighbour with given
 * {@link #receiverIP} and {@link #receiverPort}
 */
public class HeartBeat extends Thread {

    private InetAddress receiverIP;
    private int receiverPort;

    public HeartBeat(InetAddress receiverIP, int receiverPort) {
        this.receiverIP = receiverIP;
        this.receiverPort = receiverPort;
    }

    @Override
    public void run() {
        try {
            BaseRequest hb = new HeartbeatRequest(NetworkManager.getInstance().getIpAddress(),
                    NetworkManager.getInstance().getPort());
            hb.serialize();
            hb.send(receiverIP, receiverPort, new DatagramSocket());

        } catch (Exception e) {
            //TODO log
            e.printStackTrace();
        }
    }
}
