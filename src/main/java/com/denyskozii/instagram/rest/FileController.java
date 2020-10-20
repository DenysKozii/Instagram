package com.denyskozii.instagram.rest;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

@Slf4j
@RestController
@RequestMapping(value = "/image")
public class FileController {
    @Value("${image.cacheFolder}")
    private String cacheFolder;

    @GetMapping("/{type}/{imageName}")
    public ResponseEntity<byte[]> download(@PathVariable String type,
                                           @PathVariable String imageName) throws IOException {

        log.info("In controller for downloading images");
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "image/jpeg");

        String filePath = String.format("%s/%s/%s", cacheFolder, type, imageName);

        InputStream in = new FileInputStream(new File(filePath));
        byte[] media = IOUtils.toByteArray(in);
        headers.setCacheControl(CacheControl.noCache().getHeaderValue());

        ResponseEntity<byte[]> responseEntity = new ResponseEntity<>(media, headers, HttpStatus.OK);
        log.info(String.format("Download image by type %s (cropped, full) and imageName %s from local cache", type, imageName));
        return responseEntity;
    }
}
