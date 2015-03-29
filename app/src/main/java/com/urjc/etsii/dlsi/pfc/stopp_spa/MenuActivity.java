package com.urjc.etsii.dlsi.pfc.stopp_spa;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;

import com.urjc.etsii.dlsi.pfc.stopp_spa.database.StatisticsDAO;
import com.urjc.etsii.dlsi.pfc.stopp_spa.model.Movement;
import com.urjc.etsii.dlsi.pfc.stopp_spa.model.User;


public class MenuActivity extends ActionBarActivity {

    ImageButton mStatisticsButtonView;
    ImageButton mTalkButtonView;
    ImageButton mSettingsButtonView;
    ImageButton mTrainingButtonView;

    private User user;
    private StatisticsDAO statisticsDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        user = getIntent().getParcelableExtra(User.PARCELABLE_NAME);
        statisticsDAO= StatisticsDAO.getInstance(this);

        mStatisticsButtonView=(ImageButton)findViewById(R.id.statistics_button);
        mTalkButtonView=(ImageButton)findViewById(R.id.talk_button);
        mSettingsButtonView=(ImageButton)findViewById(R.id.settings_button);
        mTrainingButtonView=(ImageButton)findViewById(R.id.training_button);

        mTalkButtonView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Start the next activity
                try{
                    statisticsDAO.open();
                    statisticsDAO.registerMovement(new Movement(Movement.MovementType.LASTCONVERSATION,
                            "Conversación con el agente"),user);
                } finally{
                    if (statisticsDAO!=null && statisticsDAO.isOpen()){
                        statisticsDAO.close();
                    }
                }
                Intent mainIntent = new Intent().setClass(
                        MenuActivity.this, MainActivity.class);
                mainIntent.putExtra(User.PARCELABLE_NAME,user);
                startActivity(mainIntent);

            }
        });

        mSettingsButtonView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start the next activity
                Intent mainIntent = new Intent().setClass(
                        MenuActivity.this, SettingsActivity.class);
                mainIntent.putExtra(User.PARCELABLE_NAME,user);
                startActivity(mainIntent);
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
