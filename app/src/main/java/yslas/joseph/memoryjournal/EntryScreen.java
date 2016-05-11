package yslas.joseph.memoryjournal;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

import java.util.ArrayList;

/**
 * Created by JYsla_000 on 3/3/2016.
 */
public class EntryScreen extends FragmentActivity
{
    boolean textView = true;
    Entry viewingEnt;
    TextView entText,tag,theDate;
    GridView photos;
    Button entPhoSelection,back;
    public static EntryScreen currInstance = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.single_entry_screen);
        Intent i = getIntent();
        currInstance = this;

        entText = (TextView)findViewById(R.id.selected_entry);
        entPhoSelection = (Button)findViewById(R.id.ent_pho_selection);
        entPhoSelection.setText("See Photos");
        photos = (GridView)findViewById(R.id.ent_photos);
        tag = (TextView)findViewById(R.id.place_loc);
        theDate = (TextView)findViewById(R.id.date);
        viewingEnt = JournalMainScreen.currInstance.sendEntry(i.getExtras().getInt("id"));
        entText.setText(viewingEnt.getEntry());
        tag.setText(viewingEnt.getLocation());
        theDate.setText(viewingEnt.getEntryDate());

        back = (Button) findViewById(R.id.end);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        if (viewingEnt.getPhotos()  != null) {
            DisplayPhotos chosenPhotos = new DisplayPhotos(EntryScreen.this, viewingEnt.getPhotos());
            photos.setAdapter(chosenPhotos);
            photos.setOnItemClickListener(new OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Log.d("entry", "" + position);
                    Intent i = new Intent(EntryScreen.this,PhotoView.class);
                    i.putExtra("id",position);
                    startActivity(i);
                }
            });
        }

        entPhoSelection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (textView)
                {
                    entPhoSelection.setText("See Entry");
                    entText.setVisibility(View.INVISIBLE);
                    photos.setVisibility(View.VISIBLE);
                    textView = false;
                }
                else
                {
                    entPhoSelection.setText("See Photos");
                    entText.setVisibility(View.VISIBLE);
                    photos.setVisibility(View.INVISIBLE);
                    textView = true;
                }
            }
        });

    }
    public ArrayList<String> sendEntryPhotoList()
    {
        return this.viewingEnt.getPhotos();
    }
}
