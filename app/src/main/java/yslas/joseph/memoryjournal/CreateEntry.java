package yslas.joseph.memoryjournal;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.speech.RecognizerIntent;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;
import java.util.Calendar;
import java.text.SimpleDateFormat;

import java.util.ArrayList;
import java.util.Locale;

/**
 * Created by JYsla_000 on 3/3/2016.
 */
public class CreateEntry extends FragmentActivity
{
    protected static final int RESULT_SPEECH = 1;
    public static CreateEntry currInstance = null;


    ImageButton mic;
    EditText entry;
    String textEntry = "";
    String placeLoc = "";
    String curDate = "";
    Button addPhoto,setLoc,setDate,save,back;
    LocationManager locationManager;
    TextView locText,dateText;
    double lat;
    double longitude;
    Calendar date;
    ArrayList<String> entPhotos;
    SimpleDateFormat fixDate;


    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_entry);
        currInstance = this;
        mic = (ImageButton)findViewById(R.id.microphone_click);
        entry = (EditText)findViewById(R.id.enter_entry);
        addPhoto = (Button)findViewById(R.id.add_photos);
        locText = (TextView)findViewById(R.id.geo_tag);
        dateText = (TextView)findViewById(R.id.date);
        setDate = (Button)findViewById(R.id.set_date);
        setLoc = (Button)findViewById(R.id.set_tag);
        save = (Button)findViewById(R.id.save_entry);
        entPhotos = new ArrayList<String>();

        back = (Button) findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        locationManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);

        setLoc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               Location loc = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                if (loc == null)
                {
                    loc = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                }
                if (loc != null)
                {
                    longitude = loc.getLongitude();
                    lat = loc.getLatitude();
                }
                startActivity(new Intent(CreateEntry.this,GooglePlaces.class));
            }
        });


        mic.setOnClickListener(new View.OnClickListener() {
            @Override


            public void onClick(View v) {
                Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                        RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                intent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS,1);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.ENGLISH);
                try {
                    startActivityForResult(intent, RESULT_SPEECH);
                } catch (ActivityNotFoundException a) {
                    Toast t = Toast.makeText(getApplicationContext(),
                            "Opps! Your device doesn't support Speech to Text",
                            Toast.LENGTH_SHORT);
                    t.show();
                }
            }
        });

        addPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(CreateEntry.this, PhotoAdd.class));
            }
        });

        setDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                date = Calendar.getInstance();
                fixDate = new SimpleDateFormat(" MM/dd/yyyy ");
                curDate = fixDate.format(date.getTime());
                dateText.setText(curDate);
            }
        });

        date = Calendar.getInstance();
        fixDate = new SimpleDateFormat(" MM/dd/yyyy ");
        curDate = fixDate.format(date.getTime());

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (fixDate == null || textEntry == null || placeLoc == null) {
                    Log.d("CreateEntry", "Something is null: ");
                    try {
                        Log.d("Create Entry",  ""+ fixDate + textEntry + placeLoc);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                Log.d("loc", placeLoc);
                int entId = Database.getInstance().insetEntry(fixDate.toString(),textEntry,placeLoc,MainActivity.getCurrAccount().getEmail());

                Log.d("date", date.toString() + " " + entId);

                if ( entPhotos != null )
                    Database.getInstance().insertPhotolist(entPhotos,entId);
                finish();
            }

        });

    }

    @Override

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case RESULT_SPEECH: {
                if (resultCode == RESULT_OK && null != data) {

                    ArrayList<String> text = data
                            .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    textEntry += " " + text.get(0);
                    entry.setText(textEntry);
                }
                break;
            }

        }
    }

    private void showAlert() {
        final AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("Enable Location")
                .setMessage("Your Locations Settings is set to 'Off'.\nPlease Enable Location to " +
                        "use this app")
                .setPositiveButton("Location Settings", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                        Intent myIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        startActivity(myIntent);
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                    }
                });
        dialog.show();
    }
    private void showAlertNet() {
        final AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("Unable to connect")
                .setMessage("You must be connected to the internet.\nPlease connect to the internet ")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface paramDialogInterface, int paramInt) {

                    }
                });

        dialog.show();
    }

    public void setTextView (String location )
    {
        locText.setText(location);
        placeLoc = location;
    }

    public void setPhotos (ArrayList<String> photoAdd )
    {

        for ( String d : photoAdd )
        {
           Log.d("photos", d + " " );
           entPhotos.add(d);
        }

    }



}

