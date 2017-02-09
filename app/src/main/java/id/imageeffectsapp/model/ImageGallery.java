package id.imageeffectsapp.model;

import android.graphics.Bitmap;

/**
 * Created by Bayu WPP on 1/1/2017.
 */

public class ImageGallery {
    Bitmap bitmap;
    String fileName;

    public ImageGallery(Bitmap bitmap, String fileName) {
        this.bitmap = bitmap;
        this.fileName = fileName;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
}
