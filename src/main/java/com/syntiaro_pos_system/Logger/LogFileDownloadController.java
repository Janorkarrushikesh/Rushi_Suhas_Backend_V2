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

@RequestMapping(path = "/v2/sys/logger/")
@RestController
public class LogFileDownloadController {

    @GetMapping("/downloadLogFile")
    public ResponseEntity<Resource> downloadLogFile() throws IOException {


        Resource logFileResource = new ClassPathResource("Syntiaro.log");
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.setContentDispositionFormData("attachment", "Syntiaro.log");
        return ResponseEntity.status(HttpStatus.OK).headers(headers).body(logFileResource);

    }
}
