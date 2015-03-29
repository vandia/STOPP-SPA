package com.urjc.etsii.dlsi.pfc.stopp_spa;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.urjc.etsii.dlsi.pfc.stopp_spa.database.UserDAO;
import com.urjc.etsii.dlsi.pfc.stopp_spa.loaders.ImageAsynkLoader;
import com.urjc.etsii.dlsi.pfc.stopp_spa.model.User;
import com.urjc.etsii.dlsi.pfc.stopp_spa.utilities.DrawableUtilities;

import java.io.FileNotFoundException;


public class UploadImageActivity extends ActionBarActivity implements LoaderManager.LoaderCallbacks<Bitmap> {

    private final int SELECT_PICTURE = 0;
    private final String ERROR_MESSAGE="No se ha podido cargar la imagen";
    private ImageView imageView;
    private User user;
    private Bitmap image;
    private SaveUserImageTask mAuthTask = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_photo);
        user = getIntent().getParcelableExtra(User.PARCELABLE_NAME);
        imageView= (ImageView) findViewById(R.id.imageUploaded);
        getLoaderManager().initLoader(0, null, this);

    }


    public void pickPhoto(View view) {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,"Seleccione Imagen"), SELECT_PICTURE);

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK) {
            try {
                image = DrawableUtilities.getBitmapFromPath(data.getData(), this,
                        imageView.getWidth(), imageView.getHeight());
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                Toast.makeText(this,ERROR_MESSAGE,Toast.LENGTH_SHORT);
            }
            imageView.setImageBitmap(image);
        }
    }



    public void uploadPhoto(View view) {
        if (mAuthTask != null) {
            return;
        }
        try{
            mAuthTask= new SaveUserImageTask(this,user,image);
            mAuthTask.execute((Void) null);


        }catch (Exception e){
            imageView.requestFocus();
        }
    }

    @Override
    public Loader<Bitmap> onCreateLoader(int id, Bundle args) {
        return new ImageAsynkLoader(this, user.getName());
    }

    @Override
    public void onLoadFinished(Loader<Bitmap> loader, Bitmap data) {
        imageView.setImageBitmap(data);
    }

    @Override
    public void onLoaderReset(Loader<Bitmap> loader) {

    }

    public class SaveUserImageTask extends AsyncTask<Void, Void, Boolean> {

        private User user;
        private Bitmap image;
        private Context context;
        private UserDAO userDAO;


        public SaveUserImageTask(Context context, User user, Bitmap image) {
            this.user=user;
            this.image=image;
            this.context=context;
            this.userDAO = UserDAO.getInstance(context);
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            try {
                userDAO.open();
                userDAO.insertImage(user,image);
            }catch (Exception e){
              return false;
            }finally {
                if (userDAO!=null && userDAO.isOpen()){
                    userDAO.close();
                }
            }

           return true;

        }

        @Override
        protected void onPostExecute(Boolean success) {
            super.onPostExecute(success);
            if (success){
                NavUtils.navigateUpFromSameTask((android.app.Activity) context);
            }else{
                Toast.makeText(context,ERROR_MESSAGE,Toast.LENGTH_SHORT);
            }
        }
    }
}
