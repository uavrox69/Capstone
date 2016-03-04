package yslas.joseph.memoryjournal;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.Button;

/**
 * Created by JYsla_000 on 2/26/2016.
 * this is the main screen of the memory journal
 */
public class JournalMainScreen extends FragmentActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_journal_screen);
        Button newEntry = (Button)findViewById(R.id.new_entry);
        Button options = (Button)findViewById(R.id.options);
        Button logOut = (Button)findViewById(R.id.logout);

        newEntry.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(JournalMainScreen.this,CreateEntry.class));
            }
        });
    }
}
