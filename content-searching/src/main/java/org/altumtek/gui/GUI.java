package org.altumtek.gui;/**
 * Created by chanuka on 11/28/18.
 */

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import org.altumtek.networkmanager.NetworkManager;
import org.altumtek.networkmanager.utils.IContentSearch;

import java.io.IOException;
import java.io.PrintStream;
import java.net.InetAddress;
import java.util.List;

public class GUI extends Application {


    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {

        Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("GUI.fxml"));

        Scene scene = new Scene(root);

//        PrintStream ps = new PrintStream( taos );
//        System.setOut( ps );
//        System.setErr( ps );

        primaryStage.setScene(scene);
        primaryStage.setTitle("Distributed Search App");
        primaryStage.show();

        primaryStage.setOnCloseRequest(event -> {
            NetworkManager.getInstance().stop();
        });
    }

}
