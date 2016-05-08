package yslas.joseph.memoryjournal;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Date;

import android.content.ContentValues;
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
    private static final String PRIMKEY_PHOTO = "photo_id";
    private static final String PRIMKEY_SEC_ANS = "answer_id";
    private static final String PRIMKEY_ENTRY = "entry_id";
    private static final String COL_QUES = "quest";
    private static final String COL_A_EMAIL = "acct_email";
    private static final String COL_E_EMAIL = "acc_email";
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

    public static Database

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

    public int insetEntry ( String date, String entry, String loc, String email )
    {
        int entryID;
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_LOC,loc);
        contentValues.put(COL_DATE,date);
        contentValues.put(COL_ENTRY,entry);
        contentValues.put(COL_E_EMAIL,email);
        entryID = (int)(database.insert(TABLE_JOUR_ENT,null,contentValues));
        return entryID;
    }

    public  Entry grabEntry ( int entryKey )
    {
        Entry grabbedEnt;
        ArrayList<String>photos = grabPhotoList(entryKey);
        categoryCursor = database.query(true, TABLE_JOUR_ENT, null,
                PRIMKEY_ENTRY + "=?",  new String[] { String.valueOf(entryKey)  }, null, null, null, null);
        categoryCursor.moveToFirst();

        SimpleDateFormat formatter = new SimpleDateFormat("mm/dd/yyyy");
        String dbDate = categoryCursor.getString(categoryCursor.getColumnIndex(COL_DATE));
        Date entDate = new Date();

        try {
            entDate = formatter.parse(dbDate);
        }catch (ParseException e)
        {
            e.printStackTrace();
        }

        grabbedEnt = new Entry(categoryCursor.getString(categoryCursor.getColumnIndex(COL_ENTRY)),photos,entDate,categoryCursor.getString(categoryCursor.getColumnIndex(COL_ENTRY)));
        int ingCol = categoryCursor.getColumnIndex(PRIMKEY_ENTRY);

        return grabbedEnt;
    }

    public ArrayList<String> grabPhotoList ( int entryKey)
    {
        categoryCursor = database.query(true, TABLE_PHOTO,  new String[] { COL_PHOTO_PATH },
                PRIMKEY_ENTRY + "=?",  new String[] { String.valueOf(entryKey)  }, null, null, COL_PHOTO_PATH + " ASC", null);
        categoryCursor.moveToFirst();

        ArrayList<String> photoPaths;
        int ingCol = categoryCursor.getColumnIndex(COL_PHOTO_PATH);
        if (categoryCursor.getCount() == 0 )
            photoPaths =null;
        else {
            photoPaths = new ArrayList<String>(categoryCursor.getCount());
            try {
                do {
                    photoPaths.add(categoryCursor.getString(ingCol));
                } while (categoryCursor.moveToNext());
            } catch (Exception e) {

            }
        }
        return photoPaths;
    }

    public ArrayList<Integer> entryKeys ( String email )
    {
        categoryCursor = database.query(true, TABLE_JOUR_ENT,  new String[] { PRIMKEY_ENTRY },
                COL_E_EMAIL + "=?",  new String[] { email  }, null, null, PRIMKEY_ENTRY + " ASC", null);
        categoryCursor.moveToFirst();
        ArrayList<Integer> entryKeys;
        Log.d("count", categoryCursor.getCount() + "");
        if (categoryCursor.getCount() == 0 )
            entryKeys = null;
        else {
            entryKeys = new ArrayList<Integer>(categoryCursor.getCount());
            int ingCol = categoryCursor.getColumnIndex(PRIMKEY_ENTRY);
            try {
                do {
                    entryKeys.add(Integer.parseInt(categoryCursor.getString(ingCol)));
                } while (categoryCursor.moveToNext());
            } catch (Exception e) {

            }
        }
        return entryKeys;
    }

    public ArrayList<Integer>photoKeys ( int entryKey )
    {
        categoryCursor = database.query(true, TABLE_PHOTO,  new String[] { PRIMKEY_PHOTO },
                PRIMKEY_ENTRY + "=?",  new String[] { String.valueOf(entryKey)  }, null, null, PRIMKEY_PHOTO + " ASC", null);
        categoryCursor.moveToFirst();

        ArrayList<Integer> entryKeys = new ArrayList<Integer>(categoryCursor.getCount());
        int ingCol = categoryCursor.getColumnIndex(PRIMKEY_ENTRY);
        try {
            do {
                entryKeys.add(Integer.parseInt(categoryCursor.getString(ingCol)));
            } while (categoryCursor.moveToNext());
        } catch (Exception e) {

        }
        return entryKeys;
    }


    public void updateTextEntry ( int entryID, String entry )
    {
        entry = fixApostrophe(entry);
        database.execSQL("UPDATE " + TABLE_JOUR_ENT + " SET " + COL_ENTRY+ " = \'"
                +  entry + "\' WHERE " + PRIMKEY_ENTRY + "=" + entryID );
    }

    public void insertPhoto ( String photoPath, int entryNum )
    {
        database.execSQL("INSERT INTO " + TABLE_PHOTO + " ( " + COL_PHOTO_PATH + ", " + COL_ENT_ID + " ) VALUES ( "
                + "\'" +photoPath + "\',\'" + entryNum + "\');");
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