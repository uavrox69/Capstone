package yslas.joseph.memoryjournal;

import java.util.Date;

/**
 * Created by JYsla_000 on 3/10/2016.
 */
public class Entry
{
    private Date entryDate;
    private PhotoList photos;
    String entry;
    String location;

    public Entry ( String ent, PhotoList paths, Date entDate, String Loc)
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

    public void setPhotos (PhotoList newPaths)
    {
        this.photos = newPaths;
    }
    public PhotoList getPhotos()
    {
        return this.photos;
    }

    public void setEntry ( String ent )
    {
        this.entry = ent;
    }
    public String getEntry()
    {
        return this.entry;
    }
}
