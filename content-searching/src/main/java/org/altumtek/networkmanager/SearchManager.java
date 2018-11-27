package org.altumtek.networkmanager;

import org.altumtek.Request.RequestType;
import org.altumtek.Request.SearchRequest;
import org.altumtek.networkmanager.utils.IContentSearch;

import java.net.InetAddress;
import java.util.*;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;

public class SearchManager {
    private static final int SEARCH_HANDLE_PERIOD = 400;
    private final BlockingQueue<SearchRequest> searchRequestQueue = new LinkedBlockingQueue<>();
    private final Map<String, IContentSearch> searchQueries = new ConcurrentHashMap<>();
    private ArrayList<UUID> receivedList = new ArrayList<>();
    void start() {
        this.handleSearchRequest();
    }

    public void sendSearchRequest(String searchName, IContentSearch app) {
        searchQueries.put(searchName, app);
        SearchRequest request = new SearchRequest(
                RequestType.SER,
                searchName
                );

        for (RouteTable.Node neighbour: NetworkManager.getInstance().getRouteTable().getNeighbourList()) {
            NetworkManager.getInstance().sendMessages(
                    request,
                    neighbour.ip,
                    neighbour.port
            );
        }
    }

    private void processSearchReply(SearchRequest searchRequest) {
        String searchName = searchRequest.getSearchName();
        List<String> files = searchRequest.getFileNames();
        InetAddress fileOwner = searchRequest.getResultOwnerIP();
        int port = searchRequest.getResultOwnerPort();
        IContentSearch searchApp = searchQueries.get(searchName);
        searchApp.onSearchResults(fileOwner, port, files);
    }

    private void replySearchRequest(SearchRequest searchRequest) {
        if (receivedList.contains(searchRequest.getIdentifier())) {
            return;
        }

        receivedList.add(searchRequest.getIdentifier());
        String searchName = searchRequest.getSearchName();
        List<String> matchningFiles = new ArrayList<>(); //Todo find matching files
        if (matchningFiles.size() > 0) {
            SearchRequest replyRequest = new SearchRequest(
                    RequestType.SEROK,
                    matchningFiles,
                    searchRequest.getHopsCount() + 1
            );
            NetworkManager.getInstance().sendMessages(replyRequest,
                    searchRequest.getSearcherIP(),
                    searchRequest.getSearcherPort());
        } else {
            searchRequest.incrementHops();
            for (RouteTable.Node node: NetworkManager.getInstance().getRouteTable().getNeighbourList()) {
                NetworkManager.getInstance().sendMessages(searchRequest, node.ip, node.port);
            }
        }
    }

    private void handleSearchRequest() {
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                while (searchRequestQueue.size()>0) {
                    try {
                        SearchRequest searchRequest = searchRequestQueue.take();
                        switch (searchRequest.getType()) {
                            case SER:
                                replySearchRequest(searchRequest);
                                break;
                            case SEROK:
                                processSearchReply(searchRequest);
                                break;
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }, 0, SEARCH_HANDLE_PERIOD);

    }

    void addSearchRequest(SearchRequest searchRequest) {
        try {
            searchRequestQueue.put(searchRequest);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }



}
