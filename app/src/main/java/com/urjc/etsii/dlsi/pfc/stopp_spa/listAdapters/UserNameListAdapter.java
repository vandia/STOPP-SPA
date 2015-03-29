package com.urjc.etsii.dlsi.pfc.stopp_spa.listAdapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.urjc.etsii.dlsi.pfc.stopp_spa.R;

import java.util.List;

public class UserNameListAdapter extends BaseAdapter {

    private Context context;
    private List<String> userNameItems;


    public UserNameListAdapter(Context context, List<String> navDrawerItems) {
        this.context = context;
        this.userNameItems = navDrawerItems;
    }
 
    @Override
    public int getCount() {
        return userNameItems.size();
    }
 
    @Override
    public Object getItem(int position) {
        return userNameItems.get(position);
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
 
        String name = userNameItems.get(position);
 
        LayoutInflater mInflater = (LayoutInflater) context.getSystemService(
                Activity.LAYOUT_INFLATER_SERVICE);


        convertView = mInflater.inflate(R.layout.list_item_login,null);
        LinearLayout linearLayout= (LinearLayout) convertView.findViewById(R.id.item_login_layout);
        linearLayout.setBackgroundColor((position % 2)==1?
                context.getResources().getColor(R.color.list_item_grey):
                context.getResources().getColor(R.color.list_item_purpure));
        TextView loginText = (TextView) convertView.findViewById(R.id.item_login_text);

        loginText.setText(name);
 
        return convertView;
    }
}