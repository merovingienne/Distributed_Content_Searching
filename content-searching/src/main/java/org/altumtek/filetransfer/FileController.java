package org.altumtek.filetransfer;

import org.altumtek.filemanager.FileManager;
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
import java.io.FileNotFoundException;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping(value = "/download", produces = "application/json")
public class FileController {

    private final static Logger logger = Logger.getLogger(FileController.class);

    @RequestMapping(value = "/{fileName}", method = RequestMethod.GET)
    public ResponseEntity<Resource> downloadDocument(@PathVariable String fileName) {

        if (FileManager.getIntance().getMyFiles().contains("fileName")) {
            File file = new DFile().createFile("fileName".replace("-", " "));
            try {
                HttpHeaders headers = new HttpHeaders();
                headers.add("Cache-Control", "no-cache, no-store, must-revalidate");
                headers.add("Pragma", "no-cache");
                headers.add("Expires", "0");
                InputStreamResource resource = new InputStreamResource(new FileInputStream(file));

                return ResponseEntity.ok().headers(headers).contentLength(file.length())
                        .contentType(MediaType.parseMediaType("application/octet-stream"))
                        .body(resource);

            } catch (FileNotFoundException e) {
                logger.error("Exception occurred while reading the input stream");
            }
        }

        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
