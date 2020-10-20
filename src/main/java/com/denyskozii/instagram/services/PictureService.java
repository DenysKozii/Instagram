package com.denyskozii.instagram.services;

import com.denyskozii.instagram.dto.Picture;
import com.denyskozii.instagram.model.PictureEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.function.Function;


public interface PictureService {

    List<Picture> getPictures(String searchTerm);

    boolean savePicture(Picture info);

    Function<PictureEntity, Picture> mapToPicture = (pictureEntity -> Picture.builder()
            .id(pictureEntity.getId())
            .author(pictureEntity.getAuthor())
            .camera(pictureEntity.getCamera())
            .cropped_picture(pictureEntity.getCropped_picture())
            .full_picture(pictureEntity.getFull_picture())
            .tags(pictureEntity.getTags())
            .build());
}
