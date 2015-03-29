package com.urjc.etsii.dlsi.pfc.stopp_spa.database;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.urjc.etsii.dlsi.pfc.stopp_spa.model.Agent;
import com.urjc.etsii.dlsi.pfc.stopp_spa.model.Message;
import com.urjc.etsii.dlsi.pfc.stopp_spa.model.WorkflowTree;

/**
 * Created by vandia on 4/3/15.
 */
public class MessageDAO {
    private static MessageDAO instance = null;
    // Database fields
    private SQLiteDatabase database;
    private StoppSpaDBHelper dbHelper;
    private boolean open=false;


    private MessageDAO(Context context) {
        dbHelper = StoppSpaDBHelper.getInstance(context.getApplicationContext());
    }


    public static synchronized MessageDAO getInstance(Context c) {
        if (instance == null) {
            instance = new MessageDAO(c);
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

    public Message getMessage(WorkflowTree.PhaseCategory type, Agent agent) {

        String[] selectionArgs={Long.toString(agent.getAgentID()),type.toString()};
        Message message=null;
        Cursor cursor=null;
        try{

            cursor = database.rawQuery("SELECT ph."+StoppSpaDBHelper.PHRASES_COLUMN_PHRASE+
                    ", ph."+StoppSpaDBHelper.PHRASES_COLUMN_ID+" FROM "
                    +StoppSpaDBHelper.PHRASES_TABLE+" ph, "+StoppSpaDBHelper.AGENT_PHRASE_TABLE+" aph"
                    +", "+StoppSpaDBHelper.PHRASE_CATEGORY_TABLE+" phc WHERE aph."
                    +StoppSpaDBHelper.AGENT_PHRASE_COLUMN_PHRASEID+"=ph."+StoppSpaDBHelper.PHRASES_COLUMN_ID
                    +" AND ph."+StoppSpaDBHelper.PHRASES_COLUMN_CATEGORY_ID+"=phc."
                    +StoppSpaDBHelper.PHRASE_CATEGORY_COLUMN_ID+" AND aph."+StoppSpaDBHelper.AGENT_PHRASE_COLUMN_AGENTID
                    +"=? AND phc."+StoppSpaDBHelper.PHRASE_CATEGORY_COLUMN_NAME+"=? ORDER BY aph."+
                    StoppSpaDBHelper.AGENT_PHRASE_COLUMN_DATETIME+" ASC",selectionArgs);
            Boolean hasData = cursor.moveToFirst();
            if (hasData) {
                message=new Message( Message.Owner.AGENT,cursor.getString(0));
                database.execSQL("UPDATE "+StoppSpaDBHelper.AGENT_PHRASE_TABLE+ " SET "
                        +StoppSpaDBHelper.AGENT_PHRASE_COLUMN_DATETIME+"= CURRENT_TIMESTAMP WHERE "
                        +StoppSpaDBHelper.AGENT_PHRASE_COLUMN_AGENTID+"="+agent.getAgentID()
                        +" AND "+StoppSpaDBHelper.AGENT_PHRASE_COLUMN_PHRASEID+"="+cursor.getString(1));
            }

        }finally{
            // make sure to close the cursor
            if (cursor !=null) cursor.close();
        }

        return message;

    }

}
