package com.urjc.etsii.dlsi.pfc.stopp_spa;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Loader;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBarActivity;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.urjc.etsii.dlsi.pfc.stopp_spa.algorithm.StoppSpaAlgorithm;
import com.urjc.etsii.dlsi.pfc.stopp_spa.database.UserDAO;
import com.urjc.etsii.dlsi.pfc.stopp_spa.listAdapters.MessagesListAdapter;
import com.urjc.etsii.dlsi.pfc.stopp_spa.loaders.MessagesAsynkLoader;
import com.urjc.etsii.dlsi.pfc.stopp_spa.model.Message;
import com.urjc.etsii.dlsi.pfc.stopp_spa.model.User;
import com.urjc.etsii.dlsi.pfc.stopp_spa.utilities.DrawableUtilities;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends ActionBarActivity implements LoaderManager.LoaderCallbacks<Message> {

    private final String MESSAGE_PARAMETER="MESSAGE";
    // Chat messages list adapter
    private MessagesListAdapter adapter;
    private List<Message> listMessages;
    //Views
    private ListView listViewMessages;
    private EditText inputMsg;
    private User user;
    private StoppSpaAlgorithm stoppSpaAlgorithm;
    private Bitmap userImage;
    private GetUserImageTask mAuthTask;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        user = getIntent().getParcelableExtra(User.PARCELABLE_NAME);
        stoppSpaAlgorithm=new StoppSpaAlgorithm(this,user);
        listViewMessages = (ListView) findViewById(R.id.list_view_messages);
        inputMsg= (EditText) findViewById(R.id.inputMsg);
        listMessages = new ArrayList<Message>();
        adapter = new MessagesListAdapter(this, listMessages,user);
        listViewMessages.setAdapter(adapter);
        if (mAuthTask != null) {
            return;
        }
        try{
            mAuthTask= new GetUserImageTask(this,user.getName());
            mAuthTask.execute((Void) null);


        }catch (Exception e){
            listViewMessages.requestFocus();
        }


        Bundle args=new Bundle();
        args.putParcelable(MESSAGE_PARAMETER, (android.os.Parcelable) null);
        getLoaderManager().initLoader(0, args, this);

    }

    public void sendMessage(View view){

        String input = inputMsg.getText().toString();
        if (!TextUtils.isEmpty(input)) {
            Message m = new Message(Message.Owner.ME, input);
            appendMessage(m);
            // Clearing the input filed once message was sent
            inputMsg.setText("");
            agentAlgorithm(m);
        }
    }

    private void agentAlgorithm(Message message){

        Bundle args=new Bundle();
        args.putSerializable(MESSAGE_PARAMETER,message);
        getLoaderManager().restartLoader(0,args,this);
    }


    /**
     * Appending message to list view
     * */
    private void appendMessage(final Message m) {
        listMessages.add(m);
        adapter.notifyDataSetChanged();
    }



    @Override
    public Loader<Message> onCreateLoader(int id, Bundle args) {
        Message message= (Message) args.getSerializable(MESSAGE_PARAMETER);
        return new MessagesAsynkLoader(this,stoppSpaAlgorithm,message);
    }

    @Override
    public void onLoadFinished(Loader<Message> loader, Message data) {
        //put the message in the list view
        appendMessage(data);
    }

    @Override
    public void onLoaderReset(Loader<Message> loader) {

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


    public class GetUserImageTask extends AsyncTask<Void, Void, Bitmap> {

        private String name;
        private Context context;
        private UserDAO userDAO;


        public GetUserImageTask(Context context, String name) {
            this.name=name;
            this.context=context;
            this.userDAO = UserDAO.getInstance(context);
        }

        @Override
        protected Bitmap doInBackground(Void... params) {
            Bitmap result=null;
            try {
                userDAO.open();
                int height= DrawableUtilities.getPixelsFromDP(context,R.dimen.user_image_bubble_height);
                int width= DrawableUtilities.getPixelsFromDP(context,R.dimen.user_image_bubble_width);
                result=userDAO.getUserImage(name,width,height);
            }catch (Exception e){
                e.printStackTrace();
                return null;
            }finally {
                if (userDAO!=null && userDAO.isOpen()){
                    userDAO.close();
                }
            }

            return result;

        }

        @Override
        protected void onPostExecute(Bitmap image) {
            super.onPostExecute(image);
            userImage=image;
            adapter.setUserImage(image);
        }
    }
}
