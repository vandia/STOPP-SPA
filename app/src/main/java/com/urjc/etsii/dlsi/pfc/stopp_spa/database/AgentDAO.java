package com.urjc.etsii.dlsi.pfc.stopp_spa.database;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.urjc.etsii.dlsi.pfc.stopp_spa.model.Agent;

/**
 * Created by vandia on 21/2/15.
 */
public class AgentDAO {
    private static AgentDAO instance = null;
    // Database fields
    private SQLiteDatabase database;
    private StoppSpaDBHelper dbHelper;
    private boolean open=false;
    private Context context;


    private AgentDAO(Context context) {
        this.context = context.getApplicationContext();
        dbHelper = StoppSpaDBHelper.getInstance(context);
    }


    public static synchronized AgentDAO getInstance(Context c) {
        if (instance == null) {
            instance = new AgentDAO(c);
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


    public Agent getAgent(long id) {

        String[] columns={StoppSpaDBHelper.AGENT_COLUMN_ID,StoppSpaDBHelper.AGENT_COLUMN_NAME,
                StoppSpaDBHelper.AGENT_COLUMN_IMAGE};
        String[] selectionArgs={Long.toString(id)};
        Agent agent=null;
        Cursor cursor=null;
        try{

            cursor = database.query(StoppSpaDBHelper.AGENT_TABLE, columns,
                    StoppSpaDBHelper.AGENT_COLUMN_ID+"=?", selectionArgs, null, null, null);
            cursor.moveToFirst();
            Boolean hasData = cursor.moveToFirst();
            if (hasData) {
                int imageID=context.getResources().getIdentifier(cursor.getString(2), "drawable", context.getPackageName());
                agent=new Agent(cursor.getLong(0),cursor.getString(1),imageID);
            }

        }catch (Exception e){
            e.printStackTrace();
        }finally{
            // make sure to close the cursor
            if (cursor !=null) cursor.close();
        }

        return agent;

    }

    public boolean isOpen(){
        return open;
    }
}
