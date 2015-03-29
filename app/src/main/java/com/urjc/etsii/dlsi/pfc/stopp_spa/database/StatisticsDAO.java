package com.urjc.etsii.dlsi.pfc.stopp_spa.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.urjc.etsii.dlsi.pfc.stopp_spa.model.Movement;
import com.urjc.etsii.dlsi.pfc.stopp_spa.model.User;

/**
 * Created by vandia on 18/3/15.
 */
public class StatisticsDAO {

    private static StatisticsDAO instance = null;
    // Database fields
    private SQLiteDatabase database;
    private StoppSpaDBHelper dbHelper;
    private boolean open=false;

    private StatisticsDAO(Context context) {
        dbHelper = StoppSpaDBHelper.getInstance(context.getApplicationContext());
    }


    public static synchronized StatisticsDAO getInstance(Context c) {
        if (instance == null) {
            instance = new StatisticsDAO(c);
        }

        return instance;
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
        open=true;
    }

    public void close() {
        dbHelper.close();
        open=false;
    }

    public boolean isOpen(){
        return open;
    }

    public long registerMovement(Movement m, User user){
        ContentValues values = new ContentValues();
        values.put(StoppSpaDBHelper.STATISTICS_COLUMN_MOVEMENTTYPEID, m.getType().getID());
        values.put(StoppSpaDBHelper.STATISTICS_COLUMN_DESCRIPTION,m.getDescription());
        values.put(StoppSpaDBHelper.STATISTICS_COLUMN_USERID, user.getUserID());
        long aux=database.insertOrThrow(StoppSpaDBHelper.STATISTICS_TABLE, null,values);
        return aux;
    }
}
