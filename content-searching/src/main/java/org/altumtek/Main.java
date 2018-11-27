package org.altumtek;

import org.altumtek.networkmanager.NetworkManager;
import org.altumtek.networkmanager.utils.IContentSearch;

import java.net.InetAddress;
import java.util.List;

public class Main {
    public static void main(String args[]) {
        new SampleApplication().start();
    }

    private static class SampleApplication implements IContentSearch {
        void start() {

            // Connect to bootstrap server
            // Start finding neighbours using Gossiping
            // Start sending heartbeats
            NetworkManager.getInstance().start();

            // Send a search query
//            NetworkManager.getInstance().search("git", this);
        }

        @Override
        public void onSearchResults(InetAddress ownerAddress, int port, List<String> files) {
            // Receive results
            System.out.println("Results found");
        }
    }
}
