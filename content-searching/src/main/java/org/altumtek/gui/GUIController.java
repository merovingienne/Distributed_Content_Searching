package org.altumtek.gui;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import org.altumtek.filemanager.FileManager;
import org.altumtek.networkmanager.NetworkManager;
import org.altumtek.networkmanager.RouteTable;
import org.altumtek.networkmanager.utils.IContentSearch;

import java.net.InetAddress;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

/**
 * Created by chanuka on 11/28/18.
 */
public class GUIController implements Initializable, IContentSearch {

    @FXML
    private TextArea runningLog;

    @FXML
    private TextArea searchInput;

    @FXML
    private Button searchButton;

    @FXML
    private Button showNeighboursButton;

    @FXML
    private Button showFilesButton;

    @FXML
    private ListView<String> searchResultsList;

    @FXML
    private ListView<String> neighboursList;

    @FXML
    private ListView<String> myFilesList;

    @FXML
    private Button setIP;

    @FXML
    private TextField ipInput;

    @FXML
    private Label ipPortOutput;

    private ArrayList<String> receivedFileList;

    @FXML
    void search(ActionEvent event) {

        System.out.println("Search for");
        String search = searchInput.getText();
        System.out.println(search);
        NetworkManager.getInstance().search(search, GUIController.this);

        if (receivedFileList != null){
            receivedFileList.clear();
        }
        ListView<String> list = new ListView<String>();
        ObservableList<String> items = FXCollections.observableArrayList (receivedFileList);
        list.setItems(items);
    }

    @FXML
    void showNeighbours(ActionEvent event) {
        ObservableList<String> items = FXCollections.observableArrayList();
        int count = 1;
        for (RouteTable.Node node: NetworkManager.getInstance().getRouteTable().getNeighbourList()){
            items.add("" + count + node.getIp().getHostAddress() + " " + node.getPort());
        }

        neighboursList.setItems(items);
    }

    @FXML
    void showFiles(ActionEvent event){
        ObservableList<String> files = FXCollections.observableArrayList();
        for (String file : FileManager.getIntance().getMyFiles()) {
            files.add(file);
        }

        myFilesList.setItems(files);
    }

    @FXML
    void setIP(ActionEvent event){
        String IP = ipInput.getText();

        NetworkManager.getInstance(IP).start();

        ipPortOutput.setText(NetworkManager.getInstance().getIpPort());

        setIP.setDisable(true);

    }



    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    @Override
    public void onSearchResults(InetAddress ownerAddress, int port, List<String> files) {
        System.out.println("Files received");
        for (String file : files) {
            System.out.println(file + " ---- " + port);
        }
    }
}
