package com.daw.webapp07.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import org.springframework.web.multipart.MultipartFile;


import javax.sql.rowset.serial.SerialBlob;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Blob;
import java.sql.SQLException;


@Entity
public class Image {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Lob
    @JsonIgnore
    private Blob image;


    public Image() {
    }

    public Image(String route) {
        try {
            this.image = createBlob(route);
        } catch (SQLException | IOException e) {
            throw new RuntimeException(e);
        }
    }
    public Image(Blob image) {
        this.image = image;
    }

    public Image(MultipartFile image) {
        try {
            this.image = createBlob(image);
        } catch (SQLException | IOException e) {
            throw new RuntimeException(e);
        }
    }




    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


    public Blob getImageFile() {
        return image;
    }

    public void setImageFile(Blob image) {
        this.image = image;
    }

    private Blob createBlob(String imagePath) throws IOException, SQLException {
        try (FileInputStream fileInputStream = new FileInputStream(imagePath)) {
            byte[] data = fileInputStream.readAllBytes();
            return new javax.sql.rowset.serial.SerialBlob(data);
        }
    }

    private Blob createBlob(MultipartFile imagePath) throws IOException, SQLException {
        InputStream inputStream =  imagePath.getInputStream();
        Blob blob = new SerialBlob(inputStream.readAllBytes());
        return  blob;

    }



}
