package yslas.joseph.memoryjournal;

import android.net.Uri;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by JYsla_000 on 3/10/2016.
 */
public class Entry
{
    private Date entryDate;
    public ArrayList<String> photos= new ArrayList<String>();
    String entry;
    String location;

    public Entry ( String ent, ArrayList<String> paths, Date entDate, String loc)
    {
        this.entry = ent;
        this.entryDate = entDate;
        this.location = loc;
        this.photos = paths;
    }

    public void setEntryDate (Date newdate )
    {
        this.entryDate = newdate;
    }
    public String getEntryDate()
    {
        String curDate;
        SimpleDateFormat fixDate = new SimpleDateFormat(" MM/dd/yyyy ");
        curDate = fixDate.format(entryDate.getTime());
        return curDate;
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
            Log.d("path", "using else");
            path = null;
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
        Log.d("loc",location + " is here");
        return location;
    }


}
