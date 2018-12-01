package org.altumtek.filetransfer;

import org.altumtek.filemanager.FileManager;
import org.altumtek.networkmanager.NetworkManager;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.log4j.Logger;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.FileInputStream;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping(value = "/download", produces = "application/json")
public class FileController {

    private final static Logger logger = Logger.getLogger(FileController.class);

    @RequestMapping(value = "/{fileName}", method = RequestMethod.GET)
    public ResponseEntity<Resource> downloadDocument(@PathVariable String fileName) {

        if (FileManager.getIntance().getMyFiles().contains(fileName)) {
            try {
                File file = new DFile().createFile(fileName.replace("-", " "));
                String sha1 = DigestUtils.sha1Hex(new FileInputStream(file)).toUpperCase();
                logger.info("File Information created at the: " + NetworkManager.getInstance().getIpPort() + "\n" +
                        "File Name: " + file.getName() + "\n" +
                        "SHA1: " + sha1 + "\n" +
                        "Size of the file: " + file.length() / (1024 * 1024 * 1.0) + "\n");

                InputStreamResource resource = new InputStreamResource(new FileInputStream(file));
                return ResponseEntity.ok()
                        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=" + file.getName())
                        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_OCTET_STREAM_VALUE)
                        .header(HttpHeaders.CACHE_CONTROL, "no-cache, no-store, must-revalidate")
                        .header(HttpHeaders.PRAGMA, "no-cache")
                        .header(HttpHeaders.EXPIRES, "0")
                        .header("Checksum-SHA1", sha1)
                        .contentLength(file.length())
                        .body(resource);

            } catch (Exception e) {
                logger.error("Exception occurred while reading the input stream");
            }
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
