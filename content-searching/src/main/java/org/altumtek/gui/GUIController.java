package org.altumtek.gui;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import org.altumtek.filemanager.FileManager;
import org.altumtek.networkmanager.NetworkManager;
import org.altumtek.networkmanager.RouteTable;
import org.altumtek.networkmanager.utils.IContentSearch;

import java.net.InetAddress;
import java.net.URL;
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
    private ListView<String> filesList;

    @FXML
    void search(ActionEvent event) {

        System.out.println("Search for");
        String search = searchInput.getText();
        System.out.println(search);
        NetworkManager.getInstance().search(search, GUIController.this);

        ListView<String> list = new ListView<String>();
        ObservableList<String> items = FXCollections.observableArrayList (
                "Single", "Double", "Suite", "Family App");
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

        filesList.setItems(files);
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
