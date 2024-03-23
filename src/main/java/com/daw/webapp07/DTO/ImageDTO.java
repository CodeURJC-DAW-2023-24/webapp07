package com.daw.webapp07.DTO;

import com.daw.webapp07.model.Image;

import java.sql.Blob;
import java.sql.SQLException;
import java.util.Base64;

public class ImageDTO {
    int id;
    String base64Image;

    public ImageDTO(Image image, int id) throws SQLException {
        this.id = id;
        Blob blob = image.getImageFile();
        byte[] bytes = blob.getBytes(1, (int) blob.length());
        base64Image = Base64.getEncoder().encodeToString(bytes);
    }

    public String getBase64Image() {
        return base64Image;
    }

    public void setBase64Image(String base64Image) {
        this.base64Image = base64Image;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
