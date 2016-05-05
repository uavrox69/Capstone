package yslas.joseph.memoryjournal;

import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.net.URI;
import java.util.ArrayList;

/**
 * Created by JYsla_000 on 3/3/2016.
 */
public class PhotoAdd extends FragmentActivity
{
    Button addPhoto,takePhoto,savePhotos;
    GridView viewPhoto;
    ArrayList<String> imagesPathList;

    //trying stuff
    public static PhotoAdd currInstance = null;

    private final int PICK_IMAGE_MULTIPLE = 1;
    private final int PICK_IMAGE_SINGLE = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        imagesPathList = new ArrayList<String>();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.take_add_photos);
        currInstance = this;
        addPhoto = (Button)findViewById(R.id.Gallery);
        takePhoto = (Button)findViewById(R.id.take_photo);
        savePhotos = (Button)findViewById(R.id.save_photos);
        viewPhoto = (GridView)findViewById(R.id.grdImages);


        takePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, PICK_IMAGE_SINGLE);
            }
        });

        addPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PhotoAdd.this,CustomPhotoGalleryActivity.class);
                startActivityForResult(intent,PICK_IMAGE_MULTIPLE);
            }
        });

        savePhotos.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CreateEntry.currInstance.setPhotos(imagesPathList);
                finish();
            }
        });

    }

    protected void onResume()
    {
        super.onResume();
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if ( requestCode == PICK_IMAGE_SINGLE )
            {

                Bitmap bp = (Bitmap) data.getExtras().get("data");

                // CALL THIS METHOD TO GET THE URI FROM THE BITMAP
                Uri tempUri = data.getData();

                String photoPath = getRealPathFromURI(tempUri);
                PhotoAdd.currInstance.imagesPathList.add(photoPath);
                Log.d("photo",  "Image saved to " + photoPath  );

            }
            if (requestCode == PICK_IMAGE_MULTIPLE) {

                String[] imagesPath = data.getStringExtra("data").split("\\|");
                for (int i = 0; i < imagesPath.length; i++) {
                    Log.d("photos", imagesPath[i] );
                    imagesPathList.add(imagesPath[i]);
                }

            }
            DisplayPhotos chosenPhotos = new DisplayPhotos(PhotoAdd.this,imagesPathList);
            viewPhoto.setAdapter(chosenPhotos);
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public String getRealPathFromURI(Uri uri) {
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
        return cursor.getString(idx);
    }


    //trying
    public ArrayList<String> grablist()
    {
        return  this.imagesPathList;
    }
}
