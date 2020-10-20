package com.denyskozii.instagram.components;

import com.denyskozii.instagram.dto.Picture;
import com.denyskozii.instagram.services.PictureService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@EnableAsync
@EnableScheduling
@Slf4j
public class PictureLoader {
    @Value("${imageServer.host}")
    private String imageServerHost;
    @Value("${imageServer.authUrl}")
    private String imageServerAuth;
    @Value("${imageServer.imagesUrl}")
    private String imageServerImages;
    @Value("${imageServer.apiKey}")
    private String imageServerApiKey;
    @Value("${image.cacheFolder}")
    private String cacheFolder;

    private String pictureServerToken;
    RestTemplate restTemplate = new RestTemplate();

    @Autowired

    private PictureService pictureService;

    @PostConstruct
    public void init() throws IOException {
        Files.createDirectories(Paths.get(cacheFolder, "cropped"));
        Files.createDirectories(Paths.get(cacheFolder, "full"));
        String authUrl = String.format("%s/%s", imageServerHost, imageServerAuth);
        Map<String, String> vars = new HashMap<>();
        vars.put("apiKey", imageServerApiKey);
        Map<String, String> result = restTemplate.postForObject(authUrl, vars, Map.class);
        pictureServerToken = result.get("token");
    }


    @Scheduled(fixedDelayString = "${refreshTimeMilliseconds}")
    private void reloadCache() {
        boolean hasMore = true;
        int page = 0;
        while (hasMore) {
            String authUrl = String.format("%s/%s?page=%s", imageServerHost, imageServerImages, page + 1);
            HttpEntity entity = new HttpEntity(getHeaders());
            ResponseEntity<Map> response = restTemplate.exchange(
                    authUrl, HttpMethod.GET, entity, Map.class);
            Map<String, Object> result = response.getBody();
            List<Map<String, String>> pictures = (List<Map<String, String>>) result.get("pictures");
            pictures.forEach(p -> {
                Picture info = getImageInfo(p.get("id"));
                byte[] cropped_picture = getImage(info.getCropped_picture());
                byte[] full_picture = getImage(info.getFull_picture());
                try {
                    Files.write(Paths.get(cacheFolder,"cropped",info.getId() + ".jpg"), cropped_picture);
                    Files.write(Paths.get(cacheFolder, "full",info.getId() + ".jpg"), full_picture);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                pictureService.savePicture(info);
                log.info(String.format("Image with id %s cached", info.getId()));

            });
            hasMore = Boolean.parseBoolean(result.get("hasMore").toString());
            page = Integer.parseInt(result.get("page").toString());
        }
    }

    private Picture getImageInfo(String imageId) {
        String authUrl = String.format("%s/%s/%s", imageServerHost, imageServerImages, imageId);
        HttpEntity entity = new HttpEntity(getHeaders());
        ResponseEntity<Picture> response = restTemplate.exchange(
                authUrl, HttpMethod.GET, entity, Picture.class);
        return response.getBody();
    }

    private byte[] getImage(String imageUrl) {
        HttpHeaders httpHeaders = getHeaders();
        httpHeaders.add("Accept", "*/*");
        httpHeaders.add("Content-Type", "image/jpeg");
        HttpEntity entity = new HttpEntity(httpHeaders);
        ResponseEntity<byte[]> response = restTemplate.exchange(
                imageUrl, HttpMethod.GET, entity, byte[].class);
        return response.getBody();
    }

    private HttpHeaders getHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + pictureServerToken);
        return headers;
    }
}
