package yslas.joseph.memoryjournal;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by JYsla_000 on 2/26/2016.
 * this is the main screen of the memory journal
 */
public class JournalMainScreen extends FragmentActivity {
//todo eventually use a view pager instead of gridview to save memory, move database over?
    ArrayList<Integer> entryKeys,photoKeys;
    UserAccount userAccount;
    ArrayList<Entry>userEnts = new ArrayList<>();
    ArrayList<String> coverPhotos = new ArrayList<>();
    Button newEntry,options,logOut;
    TextView header;
    GridView entryGrid;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_journal_screen);
        newEntry = (Button)findViewById(R.id.new_entry);
        options = (Button)findViewById(R.id.options);
        logOut = (Button)findViewById(R.id.logout);
        header = (TextView)findViewById(R.id.journal_header);
        entryGrid = (GridView)findViewById(R.id.entry_grid);

        newEntry.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(JournalMainScreen.this,CreateEntry.class));
            }
        });

        userAccount = MainActivity.getCurrAccount();
        header.setText(userAccount.getUName() + "'s Journal");
        setUpEntries();


    }

    protected void onResume()
    {
        super.onResume();
        entryKeys = new ArrayList<Integer>();
        coverPhotos = new ArrayList<String>();
        setUpEntries();
    }

    void setUpEntries()
    {
        entryKeys = Database.getInstance().entryKeys(userAccount.getEmail());
        if ( entryKeys != null )
        {
            int i = 0;
            for (int e : entryKeys)
            {
                userEnts.add(Database.getInstance().grabEntry(e));
                Entry currEnt = userEnts.get(i);
                coverPhotos.add(currEnt.grabCoverPhoto());
                i++;
            }
            DisplayPhotos coverPho = new DisplayPhotos(JournalMainScreen.this,coverPhotos);
            entryGrid.setAdapter(coverPho);
        }
    }
}
