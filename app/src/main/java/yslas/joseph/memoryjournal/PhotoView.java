package yslas.joseph.memoryjournal;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import java.util.ArrayList;

/**
 * Created by JYsla_000 on 3/3/2016.
 */
public class PhotoView extends FragmentActivity
{
    ArrayList<String>entryPhotos;
    ImageView photo;
    int selectedPhoto;
    Bitmap disPhoto;
    Button next,prev,end;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.photo_view);

        entryPhotos = EntryScreen.currInstance.sendEntryPhotoList();
        photo = (ImageView)findViewById(R.id.photo);
        Intent i = getIntent();
        selectedPhoto = i.getExtras().getInt("id");
        disPhoto = BitmapFactory.decodeFile(entryPhotos.get(selectedPhoto));

        photo = (ImageView)findViewById(R.id.photo);
        photo.setImageBitmap(disPhoto);

        end = (Button)findViewById(R.id.back);
        end.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        next = (Button)findViewById(R.id.next_button);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectedPhoto == entryPhotos.size() - 1)
                    selectedPhoto = 0;
                else
                    selectedPhoto ++;
                disPhoto = BitmapFactory.decodeFile(entryPhotos.get(selectedPhoto));
                photo.setImageBitmap(disPhoto);
            }
        });

        prev = (Button)findViewById(R.id.previous_button);
        prev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectedPhoto == 0)
                    selectedPhoto = entryPhotos.size() - 1;
                else
                    selectedPhoto --;
                disPhoto = BitmapFactory.decodeFile(entryPhotos.get(selectedPhoto));
                photo.setImageBitmap(disPhoto);
            }
        });


    }
}
