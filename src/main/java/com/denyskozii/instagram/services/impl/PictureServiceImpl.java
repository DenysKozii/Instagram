package com.denyskozii.instagram.services.impl;

import com.denyskozii.instagram.dto.Picture;
import com.denyskozii.instagram.model.PictureEntity;
import com.denyskozii.instagram.repository.PictureRepository;
import com.denyskozii.instagram.services.PictureService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@Slf4j
@Transactional
public class PictureServiceImpl implements PictureService {
    private final PictureRepository pictureRepository;

    @Autowired
    public PictureServiceImpl(PictureRepository pictureRepository) {
        this.pictureRepository = pictureRepository;
    }

    @Override
    public List<Picture> getPictures(String searchTerm) {
        log.trace(String.format("Get pictures from database by %s", searchTerm));

        List<Picture> pictures = new ArrayList<>();

        pictures.addAll(
                pictureRepository.findAllByTagsContainingOrAuthorContainingOrCameraContaining(searchTerm,searchTerm,searchTerm).stream()
                .map(mapToPicture)
                .collect(Collectors.toList()));

        for (Picture picture : pictures) {
            picture.setCropped_picture("cropped/" + picture.getId() + ".jpg");
            picture.setFull_picture("full/" + picture.getId() + ".jpg");
        }
        return pictures;
    }


    @Override
    public boolean savePicture(Picture info) {
        log.trace(String.format("Save picture %s to database ", info));

        Optional<PictureEntity> pictureOption = pictureRepository.findById(info.getId());
        PictureEntity entity = pictureOption.orElse(new PictureEntity());

        entity.setId(info.getId());
        entity.setAuthor(info.getAuthor());
        entity.setCamera(info.getCamera());
        entity.setTags(info.getTags());
        entity.setCropped_picture(info.getCropped_picture());
        entity.setFull_picture(info.getFull_picture());
        pictureRepository.save(entity);
        return false;
    }
}
