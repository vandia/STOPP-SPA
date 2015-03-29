package com.urjc.etsii.dlsi.pfc.stopp_spa;

import android.app.LoaderManager.LoaderCallbacks;
import android.content.Intent;
import android.content.Loader;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.urjc.etsii.dlsi.pfc.stopp_spa.database.StatisticsDAO;
import com.urjc.etsii.dlsi.pfc.stopp_spa.database.UserDAO;
import com.urjc.etsii.dlsi.pfc.stopp_spa.listAdapters.UserNameListAdapter;
import com.urjc.etsii.dlsi.pfc.stopp_spa.loaders.NamesAsynkLoader;
import com.urjc.etsii.dlsi.pfc.stopp_spa.model.Movement;
import com.urjc.etsii.dlsi.pfc.stopp_spa.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends ActionBarActivity implements LoaderCallbacks<Map<String,String>> {



    // UI references.

    private ListView mNamesListView;

    private UserNameListAdapter adapterNames;
    private List <String> nameCollection=new ArrayList<>();
    private UserDAO userDAO;
    private UserLoginTask mAuthTask = null;
    private StatisticsDAO statisticsDAO;





    private Map<String,String> namePassword=new HashMap<String,String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mNamesListView= (ListView) findViewById(R.id.names_list_view);
        userDAO=UserDAO.getInstance(this);
        statisticsDAO=StatisticsDAO.getInstance(this);
        adapterNames =new UserNameListAdapter(this,nameCollection);
        mNamesListView.setAdapter(adapterNames);


        populateListView();


        Button mNameSignInButton = (Button) findViewById(R.id.new_user_button);
        mNameSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                // Start the next activity
                Intent mainIntent = new Intent().setClass(
                        LoginActivity.this, RegisterActivity.class);
                startActivityForResult(mainIntent,2);

            }
        });

        mNamesListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                String selection=(String)parent.getItemAtPosition(position);
                String password=namePassword.get(selection);

                if (password !=null && !TextUtils.isEmpty(password)){
                    Intent mainIntent = new Intent().setClass(
                            LoginActivity.this, PasswordActivity.class);

                    mainIntent.putExtra("name",selection);
                    mainIntent.putExtra("password",password);
                    startActivityForResult(mainIntent,1);
                }else{
                    if (mAuthTask != null) {
                        return;
                    }
                    try{
                        mAuthTask= new UserLoginTask(selection);
                        mAuthTask.execute((Void) null);


                    }catch (Exception e){
                        mNamesListView.requestFocus();
                    }
                }



            }
        });

    }

    private void populateListView() {
        getLoaderManager().initLoader(0, null, this);
    }

    @Override
    public Loader<Map<String,String>> onCreateLoader(int id, Bundle args) {
        return new NamesAsynkLoader(this);
    }

    @Override
    public void onLoadFinished(Loader<Map<String,String>> loader, Map<String,String> data) {
        namePassword=data;
        for (String pair:data.keySet()){
            nameCollection.add(pair);
        }
        adapterNames.notifyDataSetChanged();
    }

    @Override
    public void onLoaderReset(Loader<Map<String,String>> loader) {
        
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if(resultCode == RESULT_OK){
                Intent menuIntent = new Intent().setClass(
                        LoginActivity.this, MenuActivity.class);
                menuIntent.putExtra(User.PARCELABLE_NAME,
                        data.getParcelableExtra(User.PARCELABLE_NAME));
                startActivity(menuIntent);
                finish();
            }
            if (resultCode == RESULT_CANCELED) {
                //Write your code if there's no result
            }
        }
        if (requestCode == 2) {
            if(resultCode == RESULT_OK){
                Intent menuIntent = new Intent().setClass(
                        LoginActivity.this, MenuActivity.class);
                menuIntent.putExtra(User.PARCELABLE_NAME,
                        data.getParcelableExtra(User.PARCELABLE_NAME));
                startActivity(menuIntent);
                finish();
            }
            if (resultCode == RESULT_CANCELED) {
                //Write your code if there's no result
            }
        }
    }

    public class UserLoginTask extends AsyncTask<Void, Void, Boolean> {

        private final String name;
        private User user;

        UserLoginTask(String name) {
            this.name=name;
        }


        @Override
        protected Boolean doInBackground(Void... params) {

            try{
                userDAO.open();
                user=userDAO.getUser(name);
                statisticsDAO.open();
                statisticsDAO.registerMovement(new Movement(Movement.MovementType.LASTLOGIN,
                        "Registro de usuario"),user);
            } catch (Exception e) {
                return false;
            } finally{
                if (userDAO!=null && userDAO.isOpen()){
                    userDAO.close();
                }
                if (statisticsDAO!=null && statisticsDAO.isOpen()){
                    statisticsDAO.close();
                }
            }

            return true;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            mAuthTask = null;
            if (success) {
                Intent menuIntent = new Intent().setClass(
                        LoginActivity.this, MenuActivity.class);
                menuIntent.putExtra(User.PARCELABLE_NAME,user);
                startActivity(menuIntent);
                finish();

            } else {
                mNamesListView.requestFocus();
            }
        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;
        }
    }
}



