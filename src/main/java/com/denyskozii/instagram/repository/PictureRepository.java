package com.denyskozii.instagram.repository;

import com.denyskozii.instagram.model.PictureEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PictureRepository extends JpaRepository<PictureEntity, Long> {

    List<PictureEntity> findAllByTagsContainingOrAuthorContainingOrCameraContaining(String tags,String author,String camera);

//    List<PictureEntity> findAllByTagsContaining(String tags);
//
//    List<PictureEntity> findAllByAuthorContaining(String author);
//
//    List<PictureEntity> findAllByCameraContaining(String camera);

    Optional<PictureEntity> findById(String id);

}
