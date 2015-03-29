package com.urjc.etsii.dlsi.pfc.stopp_spa;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.View;

import com.urjc.etsii.dlsi.pfc.stopp_spa.database.UserDAO;
import com.urjc.etsii.dlsi.pfc.stopp_spa.model.User;


public class SettingsUserActivity extends ActionBarActivity {

    private User user;
    private UserDAO userDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings_user);
        userDAO=UserDAO.getInstance(this);
        user = getIntent().getParcelableExtra(User.PARCELABLE_NAME);
    }

    public void OnClickChangePassword(View view) {
        // Kabloey
    }
    public void OnClickChangeSecurityQuestion(View view) {
        // Kabloey
    }

    public void OnClickChangeAgent(View view) {
        // Kabloey
    }


}
