package org.altumtek.filetransfer;

import org.apache.log4j.Logger;

import javax.xml.bind.annotation.adapters.HexBinaryAdapter;
import java.io.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
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

        File file = new File(fileName);
        if (file.exists()) {
            try {
                System.out.printf("%s File Size = %.2f MB\n", name, (file.length() / (1024 * 1024)) * 1.0); //FIXME not correctly getting the size (decimal part)
                System.out.printf("SHA of the %s File = %s\n", name, sha1(file));

            } catch (NoSuchAlgorithmException e) {
                logger.error("Algorithm SHA-1 does not exists!", e);

            } catch (IOException e) {
                logger.error("Error occurred while generating the SHA value of the file", e);
            }
        }

        return file;
    }

    private String sha1(File file) throws NoSuchAlgorithmException, IOException {
        MessageDigest sha1 = MessageDigest.getInstance("SHA-1");
        InputStream fis = new FileInputStream(file);
        int n = 0;
        byte[] buffer = new byte[8192];
        while (n != -1) {
            n = fis.read(buffer);
            if (n > 0) {
                sha1.update(buffer, 0, n);
            }
        }

        return new HexBinaryAdapter().marshal(sha1.digest());
    }
}
