package org.altumtek.filetransfer;

import org.apache.log4j.Logger;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.concurrent.ThreadLocalRandom;

public class DFile {

    private final static Logger logger = Logger.getLogger(DFile.class);

    public File createFile(String name) {
        String fileName = System.getProperty("java.io.tmpdir") + File.separator + name;
        // Generate a random number between 2-10
        int fileSizeInBytes = 1024 * 1024 * ThreadLocalRandom.current().nextInt(2, 10 + 1);
        char[] chars = new char[fileSizeInBytes];
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(fileName))) {
            bw.write(chars);

        } catch (IOException e) {
            logger.error("Error occurred while creating the file: " + fileName, e);
        }
        return new File(fileName);
    }
}
