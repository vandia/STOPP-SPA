package com.urjc.etsii.dlsi.pfc.stopp_spa.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;

import com.urjc.etsii.dlsi.pfc.stopp_spa.model.User;
import com.urjc.etsii.dlsi.pfc.stopp_spa.utilities.DrawableUtilities;

import java.util.HashMap;
import java.util.Map;

import static android.database.sqlite.SQLiteDatabase.CONFLICT_REPLACE;


/**
 * Created by vandia on 2/2/15.
 */
public class UserDAO {

    private static UserDAO instance = null;
    // Database fields
    private SQLiteDatabase database;
    private final StoppSpaDBHelper dbHelper;
    private boolean open=false;
    private final Context context;


    private UserDAO(Context context) {
        dbHelper = StoppSpaDBHelper.getInstance(context.getApplicationContext());
        this.context=context;
    }


    public static synchronized UserDAO getInstance(Context c) {
        if (instance == null) {
            instance = new UserDAO(c);
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

    public User insertUser(User user) throws SQLException{
        ContentValues values = new ContentValues();
        values.put(StoppSpaDBHelper.USER_COLUMN_NAME,user.getName());
        if (user.getPassword()==null){
            values.putNull(StoppSpaDBHelper.USER_COLUMN_PWD);
        }else{
            values.put(StoppSpaDBHelper.USER_COLUMN_PWD, user.getPassword());
        }
        values.put(StoppSpaDBHelper.USER_COLUMN_AGE, user.getAge());
        values.put(StoppSpaDBHelper.USER_COLUMN_AGENTID, user.getAgent().getAgentID());
        long aux=database.insertOrThrow(StoppSpaDBHelper.USER_TABLE, null,values);
        user.setUserID(aux);

        return user;
    }

    public boolean insertImage(User user, Bitmap bitmap) throws SQLException{
        ContentValues values = new ContentValues();
        values.put(StoppSpaDBHelper.USER_COLUMN_IMAGE, DrawableUtilities.getBytes(bitmap));
        String[] selectionArgs={user.getName()};
        int aux=database.updateWithOnConflict(StoppSpaDBHelper.USER_TABLE,values,
                StoppSpaDBHelper.USER_COLUMN_NAME + " = ?",selectionArgs, CONFLICT_REPLACE);

        return aux>0;
    }

    public Map<String,String> getLoginAllUsers()throws SQLException{

        String[] columns={StoppSpaDBHelper.USER_COLUMN_NAME,StoppSpaDBHelper.USER_COLUMN_PWD};
        Cursor cursor=null;
        Map<String, String> result = new HashMap<>();
        try {
            cursor = database.query(StoppSpaDBHelper.USER_TABLE, columns, null, null, null, null, null);
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                result.put(cursor.getString(0), cursor.getString(1));
                cursor.moveToNext();
            }

        }finally {
            // make sure to close the cursor
            if (cursor !=null) cursor.close();
        }
        return result;
    }

    public User getUser(String name) throws Exception {

        String[] columns={StoppSpaDBHelper.USER_COLUMN_ID,StoppSpaDBHelper.USER_COLUMN_PWD,
                StoppSpaDBHelper.USER_COLUMN_AGE, StoppSpaDBHelper.USER_COLUMN_AGENTID};
        String[] selectionArgs={name};
        Cursor cursor=null;
        User user = null;
        AgentDAO agentDAO=null;
        try {
            cursor = database.query(StoppSpaDBHelper.USER_TABLE, columns,
                    StoppSpaDBHelper.USER_COLUMN_NAME + " = ?", selectionArgs, null, null, null);
            Boolean hasData = cursor.moveToFirst();
            if (hasData) {
                user = new User(name,cursor.getString(1));
                user.setUserID(cursor.getShort(0));
                user.setAge(cursor.getShort(2));
                agentDAO=AgentDAO.getInstance(context);
                agentDAO.open();
                user.setAgent(agentDAO.getAgent(cursor.getLong(3)));
            }
        }finally {
            // make sure to close the cursor
            if (cursor !=null) cursor.close();
            if (agentDAO !=null && agentDAO.isOpen()) agentDAO.close();

        }

        return user;

    }

    public Bitmap getUserImage(String name){
        String[] columns={StoppSpaDBHelper.USER_COLUMN_IMAGE};
        String[] selectionArgs={name};
        Cursor cursor=null;
        byte[] blob=null;
        try {
            cursor = database.query(StoppSpaDBHelper.USER_TABLE, columns,
                    StoppSpaDBHelper.USER_COLUMN_NAME + " = ?", selectionArgs, null, null, null);
            Boolean hasData = cursor.moveToFirst();
            if (hasData) {
                blob=cursor.getBlob(0);
            }
        }finally {
            // make sure to close the cursor
            if (cursor !=null) cursor.close();
        }
        return blob!=null?DrawableUtilities.getImage(blob):null;
    }

    public Bitmap getUserImage(String name,int width, int height){
        String[] columns={StoppSpaDBHelper.USER_COLUMN_IMAGE};
        String[] selectionArgs={name};
        Cursor cursor=null;
        byte[] blob=null;
        try {
            cursor = database.query(StoppSpaDBHelper.USER_TABLE, columns,
                    StoppSpaDBHelper.USER_COLUMN_NAME + " = ?", selectionArgs, null, null, null);
            Boolean hasData = cursor.moveToFirst();
            if (hasData) {
                blob=cursor.getBlob(0);
            }
        }finally {
            // make sure to close the cursor
            if (cursor !=null) cursor.close();
        }
        return blob!=null?DrawableUtilities.getImage(blob,width,height):null;
    }

    public boolean deleteUser(User user){

        String[] deleteArgs={Long.toString(user.getUserID())};
        int result=0;

        result=database.delete(StoppSpaDBHelper.USER_TABLE, StoppSpaDBHelper.USER_COLUMN_ID + " = ? "
                    , deleteArgs);

        return (result>0);
    }

    public boolean isOpen(){
        return open;
    }
}
