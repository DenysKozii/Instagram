package com.denyskozii.instagram.rest;

//import com.denyskozii.instagram.components.PictureCache;

import com.denyskozii.instagram.dto.Picture;
import com.denyskozii.instagram.services.PictureService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;

@Slf4j
@RestController
@RequestMapping(value = "/search")
public class SearchController {

    @Autowired
    private PictureService pictureService;

    @GetMapping("/{searchTerm}")
    public List<Picture> searching(@PathVariable String searchTerm) {
        log.info(String.format("Searching for images by tags, author or camera %s", searchTerm));
        return pictureService.getPictures(searchTerm);
    }

}
