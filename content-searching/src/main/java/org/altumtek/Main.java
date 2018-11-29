package org.altumtek;

import org.altumtek.gui.GUI;
import org.altumtek.networkmanager.NetworkManager;
import org.apache.log4j.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Random;

@SpringBootApplication
public class Main {

    private static final Logger logger = Logger.getLogger(Main.class);

    public static void main(String args[]) {
        int springPort = new Random().nextInt(10000) + 1200; // ports above 1200;
        logger.info("Initializing the Spring-Boot server on port:" + springPort);
        SpringApplication.run(Main.class, "--server.port=" + springPort);
        NetworkManager.setSearchPort(springPort);
        GUI.main(args);
    }
}
