package yslas.joseph.memoryjournal;

import java.io.File;
import java.util.ArrayList;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import android.database.sqlite.SQLiteOpenHelper;
import android.content.Context;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by JYsla_000 on 2/7/2016.
 */
public class Database extends SQLiteOpenHelper {
    private static final String DB_PATH = "/data/data/yslas.joseph.memoryjournal/databases/";
    private static final String DB_NAME = "mem_journal";
    public static final int DB_VERSION = 1;
    private static final String TABLE_ACCOUNT = "account";
    private static final String TABLE_SEC_QUES = "password_questions";
    private static final String TABLE_PASS_ANS = "password_answer";
    private static final String PRIMKEY_SECURITY = "quest_id";
    private static final String PRIMKEY_ACCOUNT = "email";
    private static final String PRIMKEY_SEC_ANS = "answer_id";
    private static final String COL_QUES = "question";
    private static final String COL_A_EMAIL = "acct_email";
    private static final String COL_Q_ID = "quest_id";
    private static final String COL_ANS = "answer";

    private SQLiteDatabase database;
    private final Context myContext;

    public Database(Context context) {
        super(context,DB_NAME,null,DB_VERSION);
        myContext = context;
    }
    @Override
    public void onCreate(SQLiteDatabase db) {


    }

    private boolean DBexists()
    {

        return myContext.getDatabasePath(DB_NAME).exists();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + DB_NAME);
        onCreate(db);
    }

    public void createDataBase() throws IOException {

        // By calling this method and empty database will be created into
        // the default system path
        // of your application so we are going to be able to overwrite that
        // database with our database.

        if (!DBexists())
        {
            Log.d("database", "creating db");
            try
            {
                this.getReadableDatabase();
                copyDataBase();
            }
            catch (IOException e)
            {
                throw new Error("Unable to copy database");
            }
        }
        else
        {
            Log.d("database", "db already created");
        }

    }

    /**
     * Copies your database from your local assets-folder to the just created
     * empty database in the system folder, from where it can be accessed and
     * handled. This is done by transfering bytestream.
     * */
    private void copyDataBase() throws IOException {


        // Open your local db as the input stream
        InputStream myInput = myContext.getAssets().open(DB_NAME);
        // Path to the just created empty db
        String outFileName = DB_PATH + DB_NAME;

        SQLiteDatabase db = SQLiteDatabase.openDatabase(outFileName, null, 0);
        db.execSQL("DROP TABLE IF EXISTS " + DB_NAME);
        db.close();

        // Open the empty db as the output stream
        OutputStream myOutput = new FileOutputStream(outFileName);

        // transfer bytes from the inputfile to the outputfile
        byte[] buffer = new byte[1024];
        int length;
        while ((length = myInput.read(buffer)) > 0) {
            myOutput.write(buffer, 0, length);
        }

        // Close the streams
        myOutput.flush();
        myOutput.close();
        myInput.close();
    }

    public void openDataBase() throws SQLException {

        // Open the database
        String myPath = DB_PATH + DB_NAME;
        database = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READWRITE);
    }


    @Override
    public synchronized void close() {

        if (database != null)
            database.close();

        super.close();
    }

    //this is to place the information into the database
    public  void insertAccount ( String email, String fName, String lName,String password, int q1,
                                 int q2, String a1, String a2 )
    {

        database.execSQL("INSERT INTO " + TABLE_ACCOUNT + "VALUES ( \'" + email + "\', \'" + fName +
        "\', \'" + lName + "\', \'" + password +"\' );");

        database.execSQL("INSERT INTO " + TABLE_PASS_ANS + "( " + COL_A_EMAIL + COL_Q_ID + COL_ANS +") " + "VALUES ( \'"+ email  +"\', \'" + q1 +
                "\', \'" + a1 + " \');");

        database.execSQL("INSERT INTO " + TABLE_PASS_ANS + "( " + COL_A_EMAIL + COL_Q_ID + COL_ANS+") " + "VALUES ( \'"+ email  +"\', \'" + q2 +
                "\', \'" + a2 + " \');");


    }

    //for sqlite so we can have no errors with statements
    private String fixApostrophe(String actual) {
        return actual.replace("'", "''");
    }


}