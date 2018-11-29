package org.altumtek.filetransfer;

import org.altumtek.filemanager.FileManager;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.log4j.Logger;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Arrays;

public class FileClient {

    private static final Logger logger = Logger.getLogger(FileClient.class);
    private static final String PATH = "/download";

    private FileClient() {
    }

    public static boolean download(String ipAddr, int port, String fileName) {
        if (Arrays.asList(FileManager.getFiles()).contains(fileName)) {
            File file = new File(fileName);
            CloseableHttpClient client = HttpClients.createDefault();
            try (CloseableHttpResponse response = client.execute(new HttpGet("http://" + ipAddr + ":" + port + PATH + File.separator + fileName))) {
                logger.info("Request received from the node " + ipAddr + " : " + port + "Ready to download the file: " + fileName);
                HttpEntity entity = response.getEntity();
                if (entity != null) {
                    try (FileOutputStream outStream = new FileOutputStream(file)) {
                        entity.writeTo(outStream);
                    }
                }
                logger.info("Successfully sent the file: " + fileName + " to: " + ipAddr + " : " + port);
                return true;

            } catch (Exception e) {
                logger.error("Exception occurred while downloading the file: " + fileName +
                        " from the Node: " + ipAddr + ":" + port, e);

            }
        }
        return false;
    }
}
