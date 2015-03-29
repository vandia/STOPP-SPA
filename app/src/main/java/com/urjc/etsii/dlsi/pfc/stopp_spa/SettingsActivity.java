package com.urjc.etsii.dlsi.pfc.stopp_spa;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.urjc.etsii.dlsi.pfc.stopp_spa.database.UserDAO;
import com.urjc.etsii.dlsi.pfc.stopp_spa.model.User;


public class SettingsActivity extends ActionBarActivity {

    private User user;
    private UserDAO userDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        user = getIntent().getParcelableExtra(User.PARCELABLE_NAME);
        userDAO= UserDAO.getInstance(this);
    }


    public void OnClickUserSettings(View view) {
        Intent settingsIntent = new Intent().setClass(
                SettingsActivity.this, SettingsUserActivity.class);
        settingsIntent.putExtra(User.PARCELABLE_NAME, user);
        startActivity(settingsIntent);
    }

    public void OnClickUploadImage(View view) {
        Intent settingsIntent = new Intent().setClass(
                SettingsActivity.this, UploadImageActivity.class);
        settingsIntent.putExtra(User.PARCELABLE_NAME, user);
        startActivity(settingsIntent);
    }

    public void OnClickHelp(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        // Get the layout inflater
        LayoutInflater inflater = getLayoutInflater();

        View helpLayout=inflater.inflate(R.layout.activity_help, null);

        TextView helpView = (TextView) helpLayout.findViewById(R.id.textHelp);
        helpView.setText(Html.fromHtml(getString(R.string.help_text)));

        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        builder.setView(helpLayout)
                // Add action buttons
                .setPositiveButton(R.string.settings_OK, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                });
        AlertDialog dialog = builder.create();

        dialog.show();

    }

    public void OnClickRemoveUser(View view) {
        //show an alert dialog before delete.
        // Start the next activity
        // 1. Instantiate an AlertDialog.Builder with its constructor
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        // 2. Chain together various setter methods to set the dialog characteristics
        builder.setMessage(R.string.attention_delete_text)
                .setTitle(R.string.attention_delete_title);

        // Add the buttons
        builder.setPositiveButton(R.string.settings_OK, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                try{
                    userDAO.open();
                    userDAO.deleteUser(user);
                } finally{
                    if (userDAO!=null && userDAO.isOpen()){
                        userDAO.close();
                    }
                }
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();

            }
        });
        builder.setNegativeButton(R.string.settings_Cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User cancelled the dialog
                dialog.cancel();
            }
        });

        AlertDialog dialog = builder.create();

        dialog.show();
    }


}
