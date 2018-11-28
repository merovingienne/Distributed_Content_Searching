package org.altumtek.gui;

import org.altumtek.filemanager.FileManager;
import org.altumtek.networkmanager.NetworkManager;
import org.altumtek.networkmanager.RouteTable;
import org.altumtek.networkmanager.utils.IContentSearch;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.InetAddress;
import java.util.List;

public class MainWindow implements IContentSearch {
    private JButton showNeighboursButton;
    private JPanel jPanel;
    private JButton searchButton;
    private JTextField textField1;
    private JButton showFilesButton;

    MainWindow(){
        showNeighboursButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                System.out.println("Neighbour list");
                for (RouteTable.Node node: NetworkManager.getInstance().getRouteTable().getNeighbourList()) {
                    System.out.println(node.ip+":"+node.port);
                }
            }
        });
        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                System.out.println("Search for");
                String search = textField1.getText();
                System.out.println(search);
                NetworkManager.getInstance().search(search, MainWindow.this);
            }
        });
        showFilesButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                System.out.println("My files");
                for (String file: FileManager.getIntance().getMyFiles()) {
                    System.out.println(file);
                }
            }
        });

    }

    public static void main(String[] args) {
        NetworkManager.getInstance().start();
        new Thread(() -> {
            JFrame frame = new JFrame("Calculator");
            frame.setContentPane(new MainWindow().jPanel);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.pack();
            frame.setVisible(true);
        }).start();

    }

    @Override
    public void onSearchResults(InetAddress ownerAddress, int port, List<String> files) {
        System.out.println("Files received");
        for(String file: files) {
            System.out.println(file);
        }
    }
}
