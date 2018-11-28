package org.altumtek.gui;

import org.altumtek.filemanager.FileManager;
import org.altumtek.networkmanager.NetworkManager;
import org.altumtek.networkmanager.RouteTable;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainWindow {
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
                String search = textField1.getText();
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

}
