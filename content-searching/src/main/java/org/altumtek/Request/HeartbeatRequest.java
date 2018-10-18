package org.altumtek.Request;

import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

/**
 * Heartbeat Request
 * Sent to a node's peers to announce health status.
 * <p>
 * Created by chanuka on 10/14/18.
 */
public class HeartbeatRequest extends BaseRequest {

    public HeartbeatRequest() throws Exception {
        this.type = RequestType.HEARTBEAT;
        this.senderIP = getIP();
        this.senderPort = 9484; //FIXME port given at the first instance
    }

    // TODO this works only for Linux and Windows
    // FIXME is this necessary?
    private InetAddress getIP() throws Exception {
        try (final DatagramSocket socket = new DatagramSocket()) {
            socket.connect(InetAddress.getByName("8.8.8.8"), 10002);
            return socket.getInetAddress();

        } catch (SocketException e) {
            e.printStackTrace();
            //TODO log
            throw new Exception("socket exception");

        } catch (UnknownHostException e) {
            e.printStackTrace();
            //TODO log
            throw new Exception("Unknown host exception");
        }
    }
}
