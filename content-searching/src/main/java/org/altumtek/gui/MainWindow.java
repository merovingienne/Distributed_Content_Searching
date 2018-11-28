package org.altumtek.gui;

import org.altumtek.filemanager.FileManager;
import org.altumtek.networkmanager.NetworkManager;
import org.altumtek.networkmanager.RouteTable;
import org.altumtek.networkmanager.utils.IContentSearch;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;

public class MainWindow implements IContentSearch {
    private JButton showNeighboursButton;
    private JPanel jPanel;
    private JButton searchButton;
    private JTextField textField1;
    private JButton showFilesButton;
    private JLabel ipLabel;
    private JButton exitButton;
    private JList fileSearchList;
    private JButton downloadButton;
    private JTextArea filesText;
    private JTextArea neighboursText;

    private ArrayList<SearchResult> results;

    MainWindow() {

        ipLabel.setText("IP: " + NetworkManager.getInstance().getIpPort());
        exitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                NetworkManager.getInstance().stop();
            }
        });

        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                System.out.println("Search for");
                String search = textField1.getText();
                System.out.println(search);
                NetworkManager.getInstance().search(search, MainWindow.this);
                results = new ArrayList<>();
                MainWindow.this.updateListView();
            }
        });
        downloadButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                int selectedIndex = fileSearchList.getSelectedIndex();
                MainWindow.SearchResult searchResult = results.get(selectedIndex);
//                FileClient.download(searchResult.ip.getHostAddress(),searchResult.port, searchResult.fileName);
            }
        });
        showFilesButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                System.out.println("My files");
                String text = "";
                for (String file : FileManager.getIntance().getMyFiles()) {
                    text = text + file + "\n";
                    System.out.println(file);
                }
                filesText.setText(text);
            }
        });
        showNeighboursButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                System.out.println("Neighbour list");
                String text = "";
                for (RouteTable.Node node : NetworkManager.getInstance().getRouteTable().getNeighbourList()) {
                    text = text + node.ip.getHostAddress() + " " + node.port + "\n";
                    System.out.println(node.ip + ":" + node.port);
                }
                neighboursText.setText(text);
            }
        });


    }

    public static void main(String[] args) {
        NetworkManager.getInstance().start();
        new Thread(() -> {
            JFrame frame = new JFrame("Calculator");
            frame.setMinimumSize(new Dimension(400, 600));
            frame.setContentPane(new MainWindow().jPanel);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.pack();
            frame.setVisible(true);
        }).start();

    }

    @Override
    public void onSearchResults(InetAddress ownerAddress, int port, List<String> files) {
        System.out.println("Files received");
        for (String file : files) {
            System.out.println(file + " ---- " + port);
            results.add(new SearchResult(file, ownerAddress, port));
        }
        this.updateListView();
    }

    private void updateListView() {
        DefaultListModel model = new DefaultListModel();
        for (int i = 0; i < results.size(); i++) {
            model.add(i, results.get(i).fileName);
        }
        fileSearchList.setModel(model);
    }

    {
// GUI initializer generated by IntelliJ IDEA GUI Designer
// >>> IMPORTANT!! <<<
// DO NOT EDIT OR ADD ANY CODE HERE!
        $$$setupUI$$$();
    }

    /**
     * Method generated by IntelliJ IDEA GUI Designer
     * >>> IMPORTANT!! <<<
     * DO NOT edit this method OR call it in your code!
     *
     * @noinspection ALL
     */
    private void $$$setupUI$$$() {
        jPanel = new JPanel();
        jPanel.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(12, 2, new Insets(0, 0, 0, 0), -1, -1));
        searchButton = new JButton();
        searchButton.setText("Search");
        jPanel.add(searchButton, new com.intellij.uiDesigner.core.GridConstraints(2, 0, 1, 2, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        textField1 = new JTextField();
        jPanel.add(textField1, new com.intellij.uiDesigner.core.GridConstraints(1, 0, 1, 2, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        showFilesButton = new JButton();
        showFilesButton.setText("Show Files");
        jPanel.add(showFilesButton, new com.intellij.uiDesigner.core.GridConstraints(9, 0, 1, 2, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        ipLabel = new JLabel();
        ipLabel.setText("Label");
        jPanel.add(ipLabel, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        exitButton = new JButton();
        exitButton.setText("Exit");
        jPanel.add(exitButton, new com.intellij.uiDesigner.core.GridConstraints(11, 0, 1, 2, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        fileSearchList = new JList();
        jPanel.add(fileSearchList, new com.intellij.uiDesigner.core.GridConstraints(3, 0, 1, 2, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, null, new Dimension(150, 50), null, 0, false));
        downloadButton = new JButton();
        downloadButton.setText("Download");
        jPanel.add(downloadButton, new com.intellij.uiDesigner.core.GridConstraints(4, 0, 1, 2, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        neighboursText = new JTextArea();
        neighboursText.setEditable(false);
        jPanel.add(neighboursText, new com.intellij.uiDesigner.core.GridConstraints(5, 0, 1, 2, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, null, new Dimension(150, 50), null, 0, false));
        showNeighboursButton = new JButton();
        showNeighboursButton.setText("Show neighbours");
        jPanel.add(showNeighboursButton, new com.intellij.uiDesigner.core.GridConstraints(6, 0, 2, 2, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        filesText = new JTextArea();
        filesText.setEditable(false);
        jPanel.add(filesText, new com.intellij.uiDesigner.core.GridConstraints(8, 0, 1, 2, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, null, new Dimension(150, 50), null, 0, false));
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return jPanel;
    }

    private static class SearchResult {
        String fileName;
        InetAddress ip;
        int port;

        public SearchResult(String fileName, InetAddress ip, int port) {
            this.fileName = fileName;
            this.ip = ip;
            this.port = port;
        }
    }

}
