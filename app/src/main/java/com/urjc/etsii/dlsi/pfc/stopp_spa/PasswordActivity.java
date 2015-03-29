package com.urjc.etsii.dlsi.pfc.stopp_spa;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.urjc.etsii.dlsi.pfc.stopp_spa.database.StatisticsDAO;
import com.urjc.etsii.dlsi.pfc.stopp_spa.database.UserDAO;
import com.urjc.etsii.dlsi.pfc.stopp_spa.model.Movement;
import com.urjc.etsii.dlsi.pfc.stopp_spa.model.User;
import com.urjc.etsii.dlsi.pfc.stopp_spa.utilities.EncryptUtility;


public class PasswordActivity extends Activity {

    private String userName;
    private String password;
    private EditText mPasswordView;
    private Button mLoginButtonView;
    private UserLoginTask mAuthTask = null;
    private UserDAO userDAO;
    private StatisticsDAO statisticsDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password);

        Intent intent = getIntent();
        userName= intent.getStringExtra("name");
        password= intent.getStringExtra("password");
        userDAO=UserDAO.getInstance(this);
        statisticsDAO=StatisticsDAO.getInstance(this);
        mPasswordView =(EditText) findViewById(R.id.pasword_input);
        mLoginButtonView= (Button) findViewById(R.id.login_button);

        mLoginButtonView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    String attemptPassword= EncryptUtility.encrypt(mPasswordView.getText().toString());
                    if (attemptPassword.equals(password)){
                        if (mAuthTask != null) {
                            return;
                        }
                        try{
                            mAuthTask= new UserLoginTask(userName);
                            mAuthTask.execute((Void) null);


                        }catch (Exception e){
                            mPasswordView.setError(getString(R.string.error_unexpected));
                            mPasswordView.requestFocus();
                        }

                    }else{
                        mPasswordView.setError(getString(R.string.error_incorrect_password));
                    }

                } catch (Exception e) {
                    mPasswordView.setError(getString(R.string.error_unexpected));
                }

            }
        });
    }

    public class UserLoginTask extends AsyncTask<Void, Void, Boolean> {

        private final String name;
        private User user;
        private String error=getString(R.string.error_incorrect_password);

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
                        "Login de usuario"),user);
            } catch (Exception e) {
                error=getString(R.string.error_unexpected);
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
            mPasswordView.setError(null);
            if (success) {
                Intent menuIntent = new Intent();
                menuIntent.putExtra(User.PARCELABLE_NAME,user);
                setResult(RESULT_OK,menuIntent);
                finish();

            } else {
                mPasswordView.setError(error);
                mPasswordView.requestFocus();
            }
        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;
        }
    }
}
