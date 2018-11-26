package org.altumtek.filetransfer;

import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.hateoas.ExposesResourceFor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

@CrossOrigin(origins = "*")
@RestController
@ExposesResourceFor(DFile.class)
@RequestMapping(value = "/file", produces = "application/json")
public class FileControler {

    @RequestMapping(path = "/download", method = RequestMethod.GET)
    public ResponseEntity<Resource> downloadDocument() {

        File file = new DFile().createFile("name");
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
            e.printStackTrace();
        }

        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
