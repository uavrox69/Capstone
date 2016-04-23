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
    //private static final String TABLE_SEC_QUES = "password_questions";
    private static final String TABLE_PASS_ANS = "password_answer";
    private static final String TABLE_JOUR_ENT = "journal_entry";
    private static final String TABLE_PHOTO = "photo";
    //private static final String PRIMKEY_SECURITY = "quest_id";
    private static final String PRIMKEY_ACCOUNT = "email";
    private static final String PRIMKEY_SEC_ANS = "answer_id";
    private static final String PRIMKEY_ENTRY = "entry_id";
    private static final String COL_QUES = "quest";
    private static final String COL_A_EMAIL = "acct_email";
    //private static final String COL_Q_ID = "quest";
    private static final String COL_ANS = "answer";
    private static final String COL_U_NAME = "name";
    private static final String COL_PASS = "password";
    private static final String COL_ENTRY = "entry";
    private static final String COL_LOC = "location";
    private static final String COL_DATE = "date";
    private static final String COL_PHOTO_PATH = "photo_path";
    private static final String COL_ENT_ID = "entry_id";

    private SQLiteDatabase database;
    private final Context myContext;
    private static Cursor categoryCursor;
    private static Cursor categoryCursor2;

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
    public  void insertAccount ( String email, String uName,String password, String q1,
                                 String q2, String a1, String a2 )
    {

        database.execSQL("INSERT INTO " + TABLE_ACCOUNT + " VALUES ( \'" + email + "\', \'" + uName + "\', \'" + password +"\' );");


        database.execSQL("INSERT INTO " + TABLE_PASS_ANS + "( " + COL_A_EMAIL + ", " + COL_QUES + ", " + COL_ANS +") " + " VALUES ( \'"+ email  +"\', \'" + q1 +
                "\', \'" + a1 + " \');");

        database.execSQL("INSERT INTO " + TABLE_PASS_ANS + "( " + COL_A_EMAIL +", " + COL_QUES  + ", " + COL_ANS+") " + " VALUES ( \'"+ email  +"\', \'" + q2 +
                "\', \'" + a2 + " \');");


    }

    public void insetEntry ( String date, String entry )
    {
        database.execSQL("INSERT INTO " + TABLE_JOUR_ENT + "( " + COL_DATE + ", " + COL_ENTRY +") " + " VALUES ( \'"+ date  +"\', \'" + entry + " \');");
    }

    public void updateTextEntry ( int entryID, String entry )
    {
        entry = fixApostrophe(entry);
        database.execSQL("UPDATE " + TABLE_JOUR_ENT + " SET " + COL_ENTRY+ " = \'"
                +  entry + "\' WHERE " + PRIMKEY_ENTRY + "=" + entryID );
    }

    public void insertPhoto ( String photoPath, int entryNum )
    {
        fixApostrophe(photoPath);
        database.execSQL("INSERT INTO " + TABLE_PHOTO + " ( " + COL_PHOTO_PATH + ", " + COL_ENT_ID + " ) VALUES ( "
                + photoPath + ",\'" + entryNum + "\');");
    }

    public void insertPhotolist (ArrayList<String> paths, int entryNum )
    {
        for (String d : paths)
            insertPhoto(d, entryNum);
    }

    public UserAccount getAccount(String email)
    {
        categoryCursor = database.query( TABLE_ACCOUNT,  null,
                PRIMKEY_ACCOUNT + "=?",  new String[] { email }, null, null, PRIMKEY_ACCOUNT + " ASC", null);
        categoryCursor2 = database.query( true,TABLE_PASS_ANS,  null,
                COL_A_EMAIL + "=?",  new String[] { email }, null, null, COL_A_EMAIL + " ASC", null);

        boolean emailExists = false;
        UserAccount returnAccount;

        if (categoryCursor != null && categoryCursor2 != null )
        {
            emailExists = categoryCursor.moveToFirst();
            categoryCursor2.moveToFirst();
        }
        if (emailExists) {
            String aEmail = categoryCursor.getString(categoryCursor.getColumnIndex(PRIMKEY_ACCOUNT));
            String aUname = categoryCursor.getString(categoryCursor.getColumnIndex(COL_U_NAME));
            String aPassword = categoryCursor.getString(categoryCursor.getColumnIndex(COL_PASS));
            ArrayList<String>aAnswers = new ArrayList<String>(categoryCursor2.getCount());
            ArrayList<String>aQuestions = new ArrayList<String>(categoryCursor2.getCount());

            do{
                aAnswers.add(categoryCursor2.getString(categoryCursor2.getColumnIndex(COL_ANS)));
                aQuestions.add(categoryCursor2.getString(categoryCursor2.getColumnIndex(COL_QUES)));

            }while (categoryCursor2.moveToNext());
            returnAccount = new UserAccount(aEmail,aUname,aPassword,aQuestions.get(0),aQuestions.get(1),aAnswers.get(0),aAnswers.get(1));
            return returnAccount;
        }
        else
        {
            return null;
        }



    }

    public Boolean emailExists (String email)
    {
        categoryCursor = database.query( true, TABLE_ACCOUNT,  null,
                PRIMKEY_ACCOUNT + "=?",  new String[] { email }, null, null, PRIMKEY_ACCOUNT + " ASC", null);
        return categoryCursor.getCount() > 0;
    }

    public Boolean doesMatch ( String email, String passwordTry )
    {
        String password = "";
        categoryCursor = database.query( true, TABLE_ACCOUNT,  null,
                PRIMKEY_ACCOUNT + "=?",  new String[] { email }, null, null, PRIMKEY_ACCOUNT + " ASC", null);
        if (categoryCursor.getCount() > 0)
        {
            categoryCursor.moveToFirst();
            password = categoryCursor.getString(categoryCursor.getColumnIndex(COL_PASS));
        }

        return password.equals(passwordTry);
    }

    //for sqlite so we can have no errors with statements
    private String fixApostrophe(String actual) {
        return actual.replace("'", "''");
    }


}