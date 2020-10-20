package com.denyskozii.instagram.service;

import com.denyskozii.instagram.dto.Picture;
import com.denyskozii.instagram.model.PictureEntity;
import com.denyskozii.instagram.repository.PictureRepository;
import com.denyskozii.instagram.services.PictureService;
import com.denyskozii.instagram.services.impl.PictureServiceImpl;
import org.assertj.core.util.Lists;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockitoTestExecutionListener;
import org.springframework.test.context.TestExecutionListeners;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doReturn;

@SpringBootTest
@TestExecutionListeners(MockitoTestExecutionListener.class)
@RunWith(MockitoJUnitRunner.class)
public class PictureServiceTest {

    private PictureService pictureService;

    @Mock
    private PictureRepository pictureRepository;

    PictureEntity picture;

    @Before
    public void setUp() {
        pictureService = new PictureServiceImpl(pictureRepository);

        picture = new PictureEntity();
        picture.setId("e13a844e87c749edd2fc");
        picture.setAuthor("Attentive Failure");
        picture.setCamera("Nikon D810");
        picture.setTags("#life ");
        picture.setFull_picture("full/e13a844e87c749edd2fc.jpg");
        picture.setCropped_picture("cropped/e13a844e87c749edd2fc.jpg");
        doReturn(Lists.list(picture)).when(pictureRepository).findAllByTagsContainingOrAuthorContainingOrCameraContaining("Nikon ","Nikon ","Nikon ");
    }

    @Test
    public void searchTest(){
        List<Picture> pictures = pictureService.getPictures("Nikon ");
        assertEquals(Stream.of(picture).map(pictureService.mapToPicture).collect(Collectors.toList()), pictures);
    }

}
