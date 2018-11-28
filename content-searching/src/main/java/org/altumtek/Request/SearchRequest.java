package org.altumtek.Request;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
import java.util.UUID;
import java.util.stream.Collectors;

public class SearchRequest extends BaseRequest{

    private InetAddress searcherIP;
    private int searcherPort;
    private InetAddress resultOwnerIP;
    private int resultOwnerPort;
    private String searchName;
    private List<String> fileNames;
    private int hops = 0;
    private UUID identifier;

    public SearchRequest(RequestType searchRequestType, String searchName) {
        this.type = RequestType.SER;
        this.identifier = UUID.randomUUID();
        this.message= String.format(" SER %s %d %s %d %s", senderIP.getHostAddress(), senderPort, searchName, hops, identifier.toString());
    }

    public SearchRequest(RequestType searchRequestType, List<String> files, int hops, UUID identifier) {
        this.type = RequestType.SEROK;
        String msg = files.stream().collect(Collectors.joining(" "));
        this.message = String.format(" SEROK %d %s %d %d %s ", files.size(),this.senderIP, this.senderPort, hops, identifier.toString())+msg;
    }

    public SearchRequest(String msg) throws UnknownHostException {
        StringTokenizer tokenizer = new StringTokenizer(msg, " ");
        String searchType = tokenizer.nextToken();

        if(searchType.equals("SER")) {
            this.type = RequestType.SER;
            this.searcherIP = InetAddress.getByName(tokenizer.nextToken());
            this.searcherPort = Integer.valueOf(tokenizer.nextToken());
            this.searchName = tokenizer.nextToken();
            this.hops = Integer.parseInt(tokenizer.nextToken());
            this.identifier = UUID.fromString(tokenizer.nextToken());
        } else if(searchType.equals("SEROK")) {
            this.type = RequestType.SEROK;
            int fileCount = Integer.valueOf(tokenizer.nextToken());
            this.resultOwnerIP = InetAddress.getByName(tokenizer.nextToken());
            this.resultOwnerPort = Integer.valueOf(tokenizer.nextToken());
            this.hops = Integer.parseInt(tokenizer.nextToken());
            this.identifier = UUID.fromString(tokenizer.nextToken());
            fileNames = new ArrayList<>();
            for (int i = 0; i < fileCount; i++) {
                fileNames.add(tokenizer.nextToken());
            }
        }
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

    public void prepareForward(){
        this.hops += 1;
        this.message= String.format(" SER %s %d %s %d %s",
                this.senderIP.getHostAddress(),
                this.searcherPort,
                this.searchName,
                this.hops,
                this.identifier.toString());

    }

    public int getHopsCount(){
        return this.hops;
    }

    public UUID getIdentifier(){
        return identifier;
    }
}
