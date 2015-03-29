package com.urjc.etsii.dlsi.pfc.stopp_spa.loaders;

import android.content.AsyncTaskLoader;
import android.content.Context;

import com.urjc.etsii.dlsi.pfc.stopp_spa.algorithm.StoppSpaAlgorithm;
import com.urjc.etsii.dlsi.pfc.stopp_spa.model.Message;

/**
 * Created by vandia on 9/3/15.
 */
public class MessagesAsynkLoader extends AsyncTaskLoader<Message> {

    private StoppSpaAlgorithm stoppSpaAlgorithm;
    private Message message;
    private Message result;

    public MessagesAsynkLoader(Context context,StoppSpaAlgorithm stoppSpaAlgorithm, Message message) {
        super(context);
        this.stoppSpaAlgorithm=stoppSpaAlgorithm;
        this.message=message;

    }

    @Override
    public Message loadInBackground() {
        result=stoppSpaAlgorithm.nextMessage(message);
        return result;
    }

    @Override
    protected void onStartLoading() {
        if (result != null && !takeContentChanged()) {
            deliverResult(result);
        }
        if ( result == null) {
            forceLoad();
        }
    }
}
