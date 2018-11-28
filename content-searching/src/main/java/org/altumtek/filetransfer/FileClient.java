package org.altumtek.filetransfer;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.log4j.Logger;

import java.io.File;
import java.io.FileOutputStream;

public class FileClient {

    private static final Logger logger = Logger.getLogger(FileClient.class);
    private final String PATH = "/file/download";
    private String ipAddr;
    private int port;
    private String fileName;

    public FileClient(String ipAddr, int port, String fileName) {
        this.ipAddr = ipAddr;
        this.port = port;
        this.fileName = fileName;
    }

    public boolean download() {

        File file = new File(fileName);

        CloseableHttpClient client = HttpClients.createDefault();
        try (CloseableHttpResponse response = client.execute(new HttpGet("http://" + ipAddr + ":" + port + PATH))) {
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                try (FileOutputStream outStream = new FileOutputStream(file)) {
                    entity.writeTo(outStream);
                }
            }
            return true;

        } catch (Exception e) {
            logger.error("Exception occurred while downloading the file: " + fileName +
                    " from the Node: " + ipAddr + ":" + port, e);

        }

        return false;
    }
}
