package com.urjc.etsii.dlsi.pfc.stopp_spa;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Intent;
import android.database.SQLException;
import android.database.sqlite.SQLiteConstraintException;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.urjc.etsii.dlsi.pfc.stopp_spa.database.AgentDAO;
import com.urjc.etsii.dlsi.pfc.stopp_spa.database.StatisticsDAO;
import com.urjc.etsii.dlsi.pfc.stopp_spa.database.UserDAO;
import com.urjc.etsii.dlsi.pfc.stopp_spa.model.Agent;
import com.urjc.etsii.dlsi.pfc.stopp_spa.model.Movement;
import com.urjc.etsii.dlsi.pfc.stopp_spa.model.User;


public class RegisterActivity extends ActionBarActivity {

    private UserLoginTask mAuthTask = null;
    private UserDAO userDAO;
    private AgentDAO agentDAO;

    // UI references.
    private EditText mNameView;
    private EditText mPasswordView;
    private EditText mAgeView;
    private RadioGroup mAgentsRadioView;
    private RelativeLayout mRegisterFormView;
    private ProgressBar mProgressView;
    private StatisticsDAO statisticsDAO;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        userDAO= UserDAO.getInstance(this);
        agentDAO= AgentDAO.getInstance(this);
        statisticsDAO= StatisticsDAO.getInstance(this);
        // Set up the login form.
        mNameView = (EditText) findViewById(R.id.name);
        mPasswordView = (EditText) findViewById(R.id.password);
        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.register_form || id == EditorInfo.IME_NULL) {
                    attemptRegister();
                    return true;
                }
                return false;
            }
        });
        Button mNameSignInButton = (Button) findViewById(R.id.register_button);
        mNameSignInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptRegister();
            }
        });
        mAgeView=(EditText) findViewById(R.id.text_age);
        mAgentsRadioView=(RadioGroup) findViewById(R.id.agent_radio_group);
        mRegisterFormView=(RelativeLayout) findViewById(R.id.register_form);
        mProgressView=(ProgressBar) findViewById(R.id.register_progress);
    }


    private boolean isPasswordValid(String password) {
        return (password.length() >= 4) && (password.length() <= 6);
    }

    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptRegister() {
        if (mAuthTask != null) {
            return;
        }

        // Reset errors.
        mNameView.setError(null);
        mPasswordView.setError(null);
        mAgeView.setError(null);

        // Store values at the time of the login attempt.
        String name = mNameView.getText().toString();
        String password = mPasswordView.getText().toString();
        String age=mAgeView.getText().toString();

        boolean cancel = false;
        View focusView = null;


        // Check for a valid password, if the user entered one.
        if (TextUtils.isEmpty(password)) {
            password=null;
        }

        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }

        // Check for a valid name.
        if (TextUtils.isEmpty(name)) {
            mNameView.setError(getString(R.string.error_field_required));
            focusView = mNameView;
            cancel = true;
        }

        // Check for a valid age.
        if (TextUtils.isEmpty(age)) {
            mAgeView.setError(getString(R.string.error_field_required));
            focusView = mNameView;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            try {
                User user = new User(name, password);
                user.setAge(Integer.parseInt(age));
                mAuthTask = new UserLoginTask(user,getAgentFromRadioButton());
                mAuthTask.execute((Void) null);
                showProgress(true);
            }catch (Exception e){
                mPasswordView.setError(getString(R.string.error_unexpected));
                focusView = mPasswordView;
                focusView.requestFocus();
            }


        }
    }

    private long getAgentFromRadioButton() {
        int selectedId = mAgentsRadioView.getCheckedRadioButtonId();
        // find which radioButton is checked by id
        switch (selectedId){
            case R.id.radioButtonMan:
                return 2L;
            case R.id.radioButtonWoman:
                return 1L;
        }
        return -1L;
    }

    /**
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
     */
    public class UserLoginTask extends AsyncTask<Void, Void, Boolean> {

        private final User user;
        private final long agentID;
        private String error=getString(R.string.error_incorrect_password);

        UserLoginTask(User user, long agentID) {
            this.user=user;
            this.agentID=agentID;
        }


        @Override
        protected Boolean doInBackground(Void... params) {

            try{
                agentDAO.open();
                Agent agent=agentDAO.getAgent(agentID);
                user.setAgent(agent);
                userDAO.open();
                userDAO.insertUser(user);
                Bitmap image = BitmapFactory.decodeResource(getResources(), R.drawable.user_unknown);
                userDAO.insertImage(user,image);
                statisticsDAO.open();
                statisticsDAO.registerMovement(new Movement(Movement.MovementType.LASTLOGIN,
                        "Registro de usuario"),user);
            }catch (SQLiteConstraintException e) {
                error=getString(R.string.error_name_already_exists);
                return false;
            }catch (SQLException e){
                error=getString(R.string.error_invalid_name);
                return false;
            } catch (Exception e) {
                error=getString(R.string.error_unexpected);
                return false;
            } finally{
                if (agentDAO!=null && agentDAO.isOpen()){
                    agentDAO.close();
                }
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
            showProgress(false);

            if (success) {
                Intent registerIntent = new Intent().setClass(
                        RegisterActivity.this, LoginActivity.class);
                registerIntent.putExtra(User.PARCELABLE_NAME, user);
                setResult(RESULT_OK,registerIntent);
                finish();

            } else {
                mPasswordView.setError(error);
                mPasswordView.requestFocus();
            }
        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;
            showProgress(false);
        }
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mRegisterFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mRegisterFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mRegisterFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });


            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mRegisterFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }
}
