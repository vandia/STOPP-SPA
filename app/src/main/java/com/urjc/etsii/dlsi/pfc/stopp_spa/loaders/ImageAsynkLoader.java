package com.urjc.etsii.dlsi.pfc.stopp_spa.loaders;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.graphics.Bitmap;

import com.urjc.etsii.dlsi.pfc.stopp_spa.algorithm.StoppSpaAlgorithm;
import com.urjc.etsii.dlsi.pfc.stopp_spa.database.UserDAO;
import com.urjc.etsii.dlsi.pfc.stopp_spa.model.Message;
import com.urjc.etsii.dlsi.pfc.stopp_spa.model.User;

/**
 * Created by vandia on 9/3/15.
 */
public class ImageAsynkLoader extends AsyncTaskLoader<Bitmap> {

    private String name;
    private Bitmap userImage;

    public ImageAsynkLoader(Context context,String name) {
        super(context);
        this.name=name;
    }

    @Override
    public Bitmap loadInBackground() {
        UserDAO userDAO=UserDAO.getInstance(this.getContext());
        try {
            userDAO.open();
            userImage = userDAO.getUserImage(name);
        }finally{
            userDAO.close();
        }
        return userImage;
    }

    @Override
    protected void onStartLoading() {
        if (userImage != null && !takeContentChanged()) {
            deliverResult(userImage);
        }
        if ( userImage == null) {
            forceLoad();
        }
    }
}
