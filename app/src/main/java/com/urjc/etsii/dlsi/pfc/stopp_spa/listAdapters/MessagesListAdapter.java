package com.urjc.etsii.dlsi.pfc.stopp_spa.listAdapters;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.content.res.ResourcesCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.urjc.etsii.dlsi.pfc.stopp_spa.R;
import com.urjc.etsii.dlsi.pfc.stopp_spa.model.Message;
import com.urjc.etsii.dlsi.pfc.stopp_spa.model.User;
import com.urjc.etsii.dlsi.pfc.stopp_spa.utilities.DrawableUtilities;

import java.util.List;

public class MessagesListAdapter extends BaseAdapter {
 
    private Context context;
    private List<Message> messagesItems;
    private User user;
    private Bitmap userImage;
 
    public MessagesListAdapter(Context context, List<Message> navDrawerItems, User user) {
        this.context = context;
        this.messagesItems = navDrawerItems;
        this.user=user;
    }

    public void setUserImage (Bitmap image){
        this.userImage=image;
    }

    @Override
    public int getCount() {
        return messagesItems.size();
    }
 
    @Override
    public Object getItem(int position) {
        return messagesItems.get(position);
    }
 
    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
 
        /**
         * The following list not implemented reusable list items as list items
         * are showing incorrect data Add the solution if you have one
         * */
 
        Message m = messagesItems.get(position);
 
        LayoutInflater mInflater = (LayoutInflater) context
                .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

        String name="";


        switch (messagesItems.get(position).getOwner()){
            case ME:
                // message belongs to you, so load the right aligned layout
                convertView = mInflater.inflate(R.layout.list_item_message_right,
                        null);
                name=user.getName();
                final ImageView userImageView = (ImageView) convertView.findViewById(R.id.user_image_message);
                userImageView.setImageBitmap(userImage);

                break;
            case AGENT:
                // message belongs to other person, load the left aligned layout
                convertView = mInflater.inflate(R.layout.list_item_message_left,
                        null);
                ImageView agentImage = (ImageView) convertView.findViewById(R.id.agent_image_message);

                agentImage.setImageDrawable(ResourcesCompat.getDrawable(context.getResources(),
                        user.getAgent().getImage(), null));
                name=user.getAgent().getName();
                break;
            default:
                convertView=null;
        }

 
        TextView lblFrom = (TextView) convertView.findViewById(R.id.lblMsgFrom);
        TextView txtMsg = (TextView) convertView.findViewById(R.id.txtMsg);
 
        txtMsg.setText(m.getMessage());
        lblFrom.setText(name);
 
        return convertView;
    }
}