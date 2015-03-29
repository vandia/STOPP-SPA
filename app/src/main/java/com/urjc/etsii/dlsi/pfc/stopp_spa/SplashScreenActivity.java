package com.urjc.etsii.dlsi.pfc.stopp_spa;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Window;

import com.urjc.etsii.dlsi.pfc.stopp_spa.algorithm.ApplyTemplates;
import com.urjc.etsii.dlsi.pfc.stopp_spa.algorithm.Dictionaries;
import com.urjc.etsii.dlsi.pfc.stopp_spa.algorithm.Inquiries;
import com.urjc.etsii.dlsi.pfc.stopp_spa.database.StoppSpaDBHelper;

import java.util.Timer;
import java.util.TimerTask;


public class SplashScreenActivity extends Activity {

    /**
     * Keep track of the login task to ensure we can cancel it if requested.
     */
    private InitialiseTask mAuthTask = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Set portrait orientation
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        // Hide title bar
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.activity_splash_screen);

        if (mAuthTask == null) {
            mAuthTask = new InitialiseTask(this);
            mAuthTask.execute((Void) null);
        }

    }

    public class InitialiseTask extends AsyncTask<Void, Void, Boolean> {
        Context context;

        // Set the duration of the splash screen
        private static final long SPLASH_SCREEN_DELAY = 3000;

        InitialiseTask(Context c) {
           this.context=c;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            //Initialise the database if is necessary

            StoppSpaDBHelper.getInstance(context).getReadableDatabase();
            ApplyTemplates.getInstance(context);
            Dictionaries.getInstance(context);
            Inquiries.getInstance(context);
            return true;
        }

        @Override
        protected void onPostExecute(Boolean success) {
            mAuthTask = null;

            if (success) {
                TimerTask task = new TimerTask() {
                    @Override
                    public void run() {

                        // Start the next activity
                        Intent mainIntent = new Intent().setClass(
                                SplashScreenActivity.this, LoginActivity.class);
                        startActivity(mainIntent);

                        // Close the activity so the user won't able to go back this
                        // activity pressing Back button
                        finish();
                    }
                };

                // Simulate a long loading process on application startup.
                Timer timer = new Timer();
                timer.schedule(task, SPLASH_SCREEN_DELAY);
            }else {
                finish();
            }
        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;
        }
    }


/*  @Override
    protected void onDestroy() {
        super.onDestroy();
        UnbindDrawables.unbindDrawables(findViewById(R.id.SplashScreenRootView));
        System.gc();
    }*/
}
