package yslas.joseph.memoryjournal;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by JYsla_000 on 3/10/2016.
 */
public class PhotoList {

    ArrayList<String> photos = new ArrayList<String>();

    public void PhotoList ()
    {
        photos = new ArrayList<String>();
    }

    public void addPhoto (String photoPhath)
    {
        photos.add(photoPhath);
    }

    public ArrayList<String> getPhotos()
    {
        return photos;
    }

}
