package com.syntiaro_pos_system.Logger;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.io.InputStream;

@RequestMapping(path = "/v2/sys/logger/")
@RestController
public class LogFileDownloadController {

    @GetMapping("/downloadLogFile")
    public ResponseEntity<Resource> downloadLogFile() throws IOException {
        // Load the log file from the classpath (assuming it's in src/main/resources)
        Resource logFileResource = new ClassPathResource("Syntiaro.log");

        // Set headers
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.setContentDispositionFormData("attachment", "Syntiaro.log");

        // Return the file as a ResponseEntity
        return ResponseEntity.status(HttpStatus.OK).headers(headers).body(logFileResource);

    }
}
