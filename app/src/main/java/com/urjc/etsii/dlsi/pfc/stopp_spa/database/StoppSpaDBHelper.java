package com.urjc.etsii.dlsi.pfc.stopp_spa.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.text.TextUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by vandia on 31/1/15.
 */
public class StoppSpaDBHelper extends SQLiteOpenHelper {

    private static StoppSpaDBHelper instance=null;
    private static Context context=null;

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME="stoppspa.db";
    private static final String AGENT_DATA_UPLOAD_FILE = "database/data_import.sql";

    //Agent tables and fields
    public static final String AGENT_TABLE = "AGENT";
    public static final String AGENT_COLUMN_ID="AGENTID";
    public static final String AGENT_COLUMN_NAME="NAME";
    public static final String AGENT_COLUMN_IMAGE="IMAGE";

    //User tables and fields
    public static final String USER_TABLE = "USER";
    public static final String USER_COLUMN_ID="USERID";
    public static final String USER_COLUMN_NAME="NAME";
    public static final String USER_COLUMN_PWD="PASSWORD";
    public static final String USER_COLUMN_AGE="AGE";
    public static final String USER_COLUMN_AGENTID="AGENTID";
    public static final String USER_COLUMN_IMAGE="USERIMAGE";

    //PhraseCategory tables and fields
    public static final String PHRASE_CATEGORY_TABLE = "PHRASECATEGORY";
    public static final String PHRASE_CATEGORY_COLUMN_ID ="PHRASECATEGORYID";
    public static final String PHRASE_CATEGORY_COLUMN_NAME ="NAME";
    public static final String PHRASE_CATEGORY_COLUMN_DESCRIPTION ="DESCRIPTION";

    //Phrases tables and fields
    public static final String PHRASES_TABLE = "PHRASES";
    public static final String PHRASES_COLUMN_ID ="PHRASEID";
    public static final String PHRASES_COLUMN_PHRASE ="PHRASE";
    public static final String PHRASES_COLUMN_CATEGORY_ID ="PHRASECATEGORYID";

    //Agent_Phrase tables and fields
    public static final String AGENT_PHRASE_TABLE = "AGENT_PHRASE";
    public static final String AGENT_PHRASE_COLUMN_AGENTID ="AGENTID";
    public static final String AGENT_PHRASE_COLUMN_PHRASEID ="PHRASEID";
    public static final String AGENT_PHRASE_COLUMN_DATETIME ="DATETIME";


    //MovementTypes tables and fields
    public static final String MOVEMENT_TYPES_TABLE = "MOVEMENTTYPES";
    public static final String MOVEMENT_TYPES_COLUMN_ID ="MOVEMENTTYPEID";
    public static final String MOVEMENT_TYPES_COLUMN_NAME ="NAME";
    public static final String MOVEMENT_TYPES_COLUMN_DESCRIPTION ="DESCRIPTION";


    //Statistics tables and fields
    public static final String STATISTICS_TABLE = "STATISTICS";
    public static final String STATISTICS_COLUMN_ID="MOVEID";
    public static final String STATISTICS_COLUMN_DATETIME="DATETIME";
    public static final String STATISTICS_COLUMN_MOVEMENTTYPEID="MOVEMENTTYPEID";
    public static final String STATISTICS_COLUMN_DESCRIPTION="DESCRIPTION";
    public static final String STATISTICS_COLUMN_USERID="USERID";


    //Agent table create
    private static final String AGENT_TABLE_CREATE =
            "CREATE TABLE " + AGENT_TABLE + " ( "
                    + AGENT_COLUMN_ID +" INTEGER PRIMARY KEY NOT NULL, "
                    + AGENT_COLUMN_NAME +" TEXT UNIQUE, "
                    + AGENT_COLUMN_IMAGE + " TEXT "
                    + " );";

    //User table create
    private static final String USER_TABLE_CREATE =
            "CREATE TABLE " + USER_TABLE + " ( "
                    + USER_COLUMN_ID +" INTEGER PRIMARY KEY NOT NULL, "
                    + USER_COLUMN_NAME +" TEXT UNIQUE, "
                    + USER_COLUMN_PWD + " TEXT, "
                    + USER_COLUMN_AGE +" INTEGER, "
                    + USER_COLUMN_IMAGE +" BLOB, "
                    + USER_COLUMN_AGENTID +" INTEGER, "
                    +"FOREIGN KEY("+ USER_COLUMN_AGENTID +") REFERENCES "+AGENT_TABLE+" ("+AGENT_COLUMN_ID+")"
                    + " );";

   //PhraseCategory table create
    private static final String PHRASE_CATEGORY_TABLE_CREATE =
            "CREATE TABLE " + PHRASE_CATEGORY_TABLE + " ( "
                    + PHRASE_CATEGORY_COLUMN_ID +" INTEGER PRIMARY KEY NOT NULL, "
                    + PHRASE_CATEGORY_COLUMN_NAME +" TEXT UNIQUE, "
                    + PHRASE_CATEGORY_COLUMN_DESCRIPTION + " TEXT "
                    + " );";

    //Phrases table create
    private static final String PHRASES_TABLE_CREATE =
            "CREATE TABLE " + PHRASES_TABLE + " ( "
                    + PHRASES_COLUMN_ID +" INTEGER PRIMARY KEY NOT NULL, "
                    + PHRASES_COLUMN_PHRASE +" TEXT UNIQUE, "
                    + PHRASES_COLUMN_CATEGORY_ID + " INTEGER, "
                    +"FOREIGN KEY("+ PHRASES_COLUMN_CATEGORY_ID +") REFERENCES "+PHRASE_CATEGORY_TABLE+" ("+ PHRASE_CATEGORY_COLUMN_ID +")"
                    + " );";

    //Agent_Phrase table create
    private static final String AGENT_PHRASE_TABLE_CREATE =
            "CREATE TABLE " + AGENT_PHRASE_TABLE + " ( "
                    + AGENT_PHRASE_COLUMN_AGENTID +" INTEGER NOT NULL, "
                    + AGENT_PHRASE_COLUMN_PHRASEID +" INTEGER NOT NULL, "
                    + AGENT_PHRASE_COLUMN_DATETIME +" DATETIME DEFAULT CURRENT_TIMESTAMP, "
                    +"PRIMARY KEY("+AGENT_PHRASE_COLUMN_AGENTID+","+AGENT_PHRASE_COLUMN_PHRASEID+"),"
                    +"FOREIGN KEY("+ AGENT_PHRASE_COLUMN_AGENTID +") REFERENCES "+AGENT_TABLE+" ("+AGENT_COLUMN_ID+"),"
                    +"FOREIGN KEY("+ AGENT_PHRASE_COLUMN_PHRASEID +") REFERENCES "+PHRASES_TABLE+" ("+ PHRASES_COLUMN_ID +")"
                    + " );";


    //MovementTypes table create
    private static final String MOVEMENT_TYPES_TABLE_CREATE =
            "CREATE TABLE " + MOVEMENT_TYPES_TABLE + " ( "
                    + MOVEMENT_TYPES_COLUMN_ID +" INTEGER PRIMARY KEY NOT NULL, "
                    + MOVEMENT_TYPES_COLUMN_NAME +" TEXT UNIQUE, "
                    + MOVEMENT_TYPES_COLUMN_DESCRIPTION + " TEXT "
                    + " );";


    //Statistics table create
    private static final String STATISTICS_TABLE_CREATE =
            "CREATE TABLE " + STATISTICS_TABLE + " ( "
                    + STATISTICS_COLUMN_ID +" INTEGER PRIMARY KEY NOT NULL, "
                    + STATISTICS_COLUMN_DATETIME +" DATETIME DEFAULT CURRENT_TIMESTAMP, "
                    + STATISTICS_COLUMN_DESCRIPTION + " TEXT, "
                    + STATISTICS_COLUMN_MOVEMENTTYPEID +" INTEGER, "
                    + STATISTICS_COLUMN_USERID +" INTEGER, "
                    +"FOREIGN KEY("+ STATISTICS_COLUMN_MOVEMENTTYPEID +") REFERENCES "+MOVEMENT_TYPES_TABLE+" ("+ MOVEMENT_TYPES_COLUMN_ID +"),"
                    +"FOREIGN KEY("+ STATISTICS_COLUMN_USERID +") REFERENCES "+USER_TABLE+" ("+USER_COLUMN_ID+")"
                    + " );";

    private StoppSpaDBHelper(Context context) {
        super(context.getApplicationContext(), DATABASE_NAME, null, DATABASE_VERSION);
        this.context=context.getApplicationContext();
    }

    public static synchronized StoppSpaDBHelper getInstance(Context context) {
        if (instance == null) {
            instance = new StoppSpaDBHelper(context);
        }

        return instance;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(AGENT_TABLE_CREATE);
        db.execSQL(PHRASE_CATEGORY_TABLE_CREATE);
        db.execSQL(PHRASES_TABLE_CREATE);
        db.execSQL(AGENT_PHRASE_TABLE_CREATE);
        db.execSQL(USER_TABLE_CREATE);
        db.execSQL(MOVEMENT_TYPES_TABLE_CREATE);
        db.execSQL(STATISTICS_TABLE_CREATE);
        dataUpload(AGENT_DATA_UPLOAD_FILE,db);
//        dataUpload("database/data_prueba.sql",db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    private void dataUpload(String filePath, SQLiteDatabase db){
        InputStream is = null;
        try {
            is = context.getAssets().open(filePath);
            if (is != null) {
                db.beginTransaction();
                BufferedReader reader = new BufferedReader(new InputStreamReader(is));
                String line = reader.readLine();
                while (!TextUtils.isEmpty(line)) {
                    db.execSQL(line);
                    line = reader.readLine();
                }
                db.setTransactionSuccessful();
            }
        } catch (Exception ex) {
            // Muestra log
            ex.printStackTrace();
        } finally {
            db.endTransaction();
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    // Muestra log
                    e.printStackTrace();
                }
            }
        }
    }

}
