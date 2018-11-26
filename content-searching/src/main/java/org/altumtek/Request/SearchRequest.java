package org.altumtek.Request;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
import java.util.stream.Collectors;

public class SearchRequest extends BaseRequest{

    public enum SearchRequestType {
        SEARCH,
        RESULT
    }

    private SearchRequestType searchRequestType;
    private InetAddress searcherIP;
    private int searcherPort;
    private InetAddress resultOwnerIP;
    private int resultOwnerPort;
    private String searchName;
    private List<String> fileNames;

    public SearchRequest(SearchRequestType searchRequestType, InetAddress senderIP, int senderPort, String searchName) {
        this.type = RequestType.SEARCH;
        //Todo
//        if (searchRequestType != SearchRequestType.SEARCH)
//            throw new InvalidRequestException ()
        this.searchRequestType = SearchRequestType.SEARCH;
        String msg = String.format("SEARCH-%s-%d-%s", senderIP.getHostAddress(), senderPort, searchName);
        super.data.put("msg", msg);
    }

    public SearchRequest(SearchRequestType searchRequestType, List<String> files, InetAddress fileOwnerIP, int fileOwnerPort, String searchName) {
        this.type = RequestType.SEARCH;
        this.searchRequestType = searchRequestType;
        //Todo
//        if (searchRequestType != SearchRequestType.SEARCH)
//            throw new InvalidRequestException ()
        String msg = files.stream().collect(Collectors.joining("-"));
        msg = String.format("RESULT-%s-%d-%s-%d",fileOwnerIP.getHostAddress(),fileOwnerPort, searchName, files.size())+msg;
        super.data.put("msg", msg);
    }

    public SearchRequest(String msg) throws UnknownHostException {
        StringTokenizer tokenizer = new StringTokenizer(msg, "-");
        String searchType = tokenizer.nextToken();

        if(searchType.equals("SEARCH")) {
            this.searchName = tokenizer.nextToken();
            this.searcherIP = InetAddress.getByName(tokenizer.nextToken());
            this.searcherPort = Integer.valueOf(tokenizer.nextToken());
        } else if(searchType.equals("RESULT")) {
            this.resultOwnerIP = InetAddress.getByName(tokenizer.nextToken());
            this.resultOwnerPort = Integer.valueOf(tokenizer.nextToken());
            this.searchName = tokenizer.nextToken();
            fileNames = new ArrayList<>();
            int fileCount = Integer.valueOf(tokenizer.nextToken());
            for (int i = 0; i < fileCount; i++) {
                fileNames.add(tokenizer.nextToken());
            }
        }
    }

    public SearchRequestType getSearchRequestType() {
        return searchRequestType;
    }

    public InetAddress getSearcherIP() {
        return searcherIP;
    }

    public int getSearcherPort() {
        return searcherPort;
    }

    public InetAddress getResultOwnerIP() {
        return resultOwnerIP;
    }

    public int getResultOwnerPort() {
        return resultOwnerPort;
    }

    public String getSearchName() {
        return searchName;
    }

    public List<String> getFileNames() {
        return fileNames;
    }
}
