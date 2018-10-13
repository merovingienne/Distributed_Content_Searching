package org.altumtek;

import java.io.IOException;
import java.net.*;

/**
 * Created by chanuka on 10/12/18.
 */
public class EchoClient extends Thread {
    private DatagramSocket socket;
    private InetAddress address;
    private boolean running;

    private byte[] buf;
    private byte[] rM;

    public EchoClient() {
        try {
            socket = new DatagramSocket(55345);
        } catch (SocketException e) {
            e.printStackTrace();
        }

        try {
            address = InetAddress.getByName("localhost");
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }

    public void run() {
        running = true;

        while (running) {
            rM = new byte[256];
            DatagramPacket receivedPacket = new DatagramPacket(rM, rM.length);
            try {
                socket.receive(receivedPacket);
                String received = new String(receivedPacket.getData(), 0, receivedPacket.getLength());
                System.out.println("received at client: " + received);
                running = false;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public String sendEcho(String msg) {
        buf = msg.getBytes();
        DatagramPacket packet
                = new DatagramPacket(buf, buf.length, address, 4445);
//                = new DatagramPacket(buf, buf.length, address, 55555); // change between echo server & Bootstrap server.
        try {
            socket.send(packet);
        } catch (IOException e) {
            e.printStackTrace();
        }
        String received = "end";
        if (!msg.equalsIgnoreCase("end")) {
            this.run();
            received = new String(rM, 0, rM.length);
        }
        running = false;
        return received;
    }

    public void close() {
        running = false;
        socket.close();
    }
}
