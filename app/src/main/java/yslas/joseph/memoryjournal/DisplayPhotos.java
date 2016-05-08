package yslas.joseph.memoryjournal;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by JYsla_000 on 4/22/2016.
 */
public class DisplayPhotos extends BaseAdapter {
    private Context mContext;
    private final ArrayList<String> photos;
    Bitmap disPhoto;

    public DisplayPhotos(Context c,ArrayList<String> photos ) {
        mContext = c;
        this.photos = photos;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return photos.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        View grid;
        LayoutInflater inflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (convertView == null) {

            grid = new View(mContext);
            grid = inflater.inflate(R.layout.custom_gallery_item, null);
            Log.d("path", position + " ");
            if (photos.get(position) == null )
            {
                disPhoto = BitmapFactory.decodeResource(this.mContext.getResources(), R.drawable.photo_holder);
            }
            else {
                disPhoto = BitmapFactory.decodeFile(photos.get(position));
            }
            disPhoto = Bitmap.createScaledBitmap(disPhoto,(int)(disPhoto.getWidth() *0.05),(int)(disPhoto.getHeight() *0.05),true);
            ImageView imageView = (ImageView)grid.findViewById(R.id.imgThumb);


            imageView.setImageBitmap(disPhoto);

        } else {
            grid = (View) convertView;
        }

        return grid;
    }

}

