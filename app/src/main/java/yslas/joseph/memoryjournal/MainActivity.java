package yslas.joseph.memoryjournal;

import android.content.Intent;
import android.database.SQLException;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.IOException;


public class MainActivity extends FragmentActivity  {

    Database db = new Database(this);
    UserAccount currAccount;

    public static MainActivity currInstance = null;

    String email;
    String pass;
    Toast message;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_screen);
        Button create = (Button)(findViewById(R.id.create_account));
        Button login = (Button) (findViewById(R.id.login_button));
        currInstance = this;

        try {
            db.createDataBase();
        } catch (IOException ioe) {
            throw new Error("Unable to create database");
        }
        try {
            db.openDataBase();
        } catch (SQLException sqle) {
            throw sqle;
        }

        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,CreateAccount.class));
            }
        });
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                verifyAccount();
            }
        });
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

    //getDb:
    //  Returns the currently opened database
    public Database getDb() {
        return db;
    }


    //fill the account info
    public void fillAccount(UserAccount filledOut)
    {
        this.currAccount = filledOut;
    }
    //get current account
    public UserAccount getCurrAccount()
    {
        return this.currAccount;
    }

    public void verifyAccount()
    {
        email = ((EditText)findViewById(R.id.login_entry)).getText().toString();
        pass = ((EditText)findViewById(R.id.login_password)).getText().toString();
        if (db.doesMatch(email.toLowerCase(),pass))
        {
            currAccount = db.getAccount(email.toLowerCase());
            startActivity(new Intent(MainActivity.this,JournalMainScreen.class));
        }
        else
        {
            message = Toast.makeText(this, "Password or Email does not match", Toast.LENGTH_LONG);
            message.setGravity(Gravity.CENTER, 0, 0);
            message.show();
        }
    }
}



