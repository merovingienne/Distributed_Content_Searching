package org.altumtek;

import org.altumtek.gui.GUI;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Main {
    public static void main(String args[]) {
        SpringApplication.run(Main.class, args);
        GUI.main(args);
    }
}
