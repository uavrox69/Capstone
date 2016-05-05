package yslas.joseph.memoryjournal;

import android.net.Uri;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by JYsla_000 on 3/10/2016.
 */
public class Entry
{
    private Date entryDate;
    public ArrayList<String> photos;
    String entry;
    String location;

    public Entry ( String ent, ArrayList<String> paths, Date entDate, String Loc)
    {
        this.entry = ent;
        this.entryDate = entDate;
        this.location = location;
        this.photos = paths;
    }

    public void setEntryDate (Date newdate )
    {
        this.entryDate = newdate;
    }
    public Date getEntryDate()
    {
        return this.entryDate;
    }

    public void setPhotos (ArrayList<String> newPaths)
    {
        this.photos = newPaths;
    }
    public ArrayList<String> getPhotos()
    {
        return this.photos;
    }
    public String grabCoverPhoto()
    {
        String path;
        if (photos != null )
            path = photos.get(0);
        else {
            Uri fileUri = Uri.parse("android.resource://yslas.joseph.memoryjournal/"+R.drawable.sunset_background);
            path = fileUri.getPath();
        }
        return path;
    }

    public void setEntry ( String ent )
    {
        this.entry = ent;
    }
    public String getEntry()
    {
        return this.entry;
    }

    public void setLocation(String location )
    {
        this.location = location;
    }
    public String getLocation()
    {
        return this.location;
    }
}
