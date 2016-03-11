package yslas.joseph.memoryjournal;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by JYsla_000 on 3/10/2016.
 */
public class PhotoList {

    private List<String> photos = new ArrayList<String>();

    public void addPhoto (String photoPhath)
    {
        photos.add(photoPhath);
    }

    public List<String> getPhotos()
    {
        return photos;
    }

}
