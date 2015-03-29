package com.urjc.etsii.dlsi.pfc.stopp_spa.loaders;

import android.content.AsyncTaskLoader;
import android.content.Context;

import com.urjc.etsii.dlsi.pfc.stopp_spa.database.UserDAO;

import java.util.Map;


/**
 * Created by vandia on 13/02/15.
 */
public class NamesAsynkLoader extends AsyncTaskLoader<Map<String, String>> {

    private Map<String,String> namesPasswords;

    public NamesAsynkLoader(Context context) {
        super(context);
    }

    @Override
    public Map<String,String> loadInBackground() {
        UserDAO userDAO=UserDAO.getInstance(this.getContext());
        try {
            userDAO.open();
            namesPasswords = userDAO.getLoginAllUsers();
        }finally{
            userDAO.close();
        }
        return namesPasswords;
    }

    @Override

    protected void onStartLoading() {
        if (namesPasswords != null && !takeContentChanged()) {
            deliverResult(namesPasswords);
        }
        if ( namesPasswords == null) {
            forceLoad();
        }
    }
}
