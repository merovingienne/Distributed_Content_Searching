package org.altumtek.networkmanager;

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

    void start() {
        this.handleSearchRequest();
    }

    public void sendSearchRequest(String searchName, IContentSearch app) {
        searchQueries.put(searchName, app);
        SearchRequest request = new SearchRequest(
                SearchRequest.SearchRequestType.SEARCH,
                NetworkManager.getInstance().getIpAddress(),
                NetworkManager.getInstance().getPort(),
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
        String searchName = searchRequest.getSearchName();
        List<String> matchningFiles = new ArrayList<>(); //Todo find matching files
        SearchRequest replyRequest = new SearchRequest(
                SearchRequest.SearchRequestType.RESULT,
                matchningFiles,
                NetworkManager.getInstance().getIpAddress(),
                NetworkManager.getInstance().getPort(),
                searchRequest.getSearchName()
        );
        NetworkManager.getInstance().sendMessages(replyRequest,
                searchRequest.getSearcherIP(),
                searchRequest.getSearcherPort());
    }

    private void handleSearchRequest() {
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                while (searchRequestQueue.size()>0) {
                    try {
                        SearchRequest searchRequest = searchRequestQueue.take();
                        switch (searchRequest.getSearchRequestType()) {
                            case SEARCH:
                                replySearchRequest(searchRequest);
                                break;
                            case RESULT:
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
