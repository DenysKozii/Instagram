package com.denyskozii.instagram.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "pictures")
public class PictureEntity {

    @Id
    private String id;

    private String author;

    private String camera;

    private String tags;

    private String cropped_picture;

    private String full_picture;

}
