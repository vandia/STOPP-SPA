package com.urjc.etsii.dlsi.pfc.stopp_spa.model;


import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by vandia on 21/2/15.
 */
public class Agent implements Parcelable{
    private long agentID;
    private String name;
    private int image;


    public Agent(long agentID, String name, int image){
        this.agentID =agentID;
        this.name=name;
        this.image=image;

    }
    private Agent(Parcel in){

        agentID = in.readLong();
        name = in.readString();
        image = in.readInt();

    }

    public long getAgentID() {
        return agentID;
    }

    public void setAgentID(long agentID) {
        this.agentID = agentID;
    }


    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeLong(agentID);
        dest.writeString(name);
        dest.writeInt(image);

    }

    // this is used to regenerate your object. All Parcelables must have a CREATOR that implements these two methods
    public static final Parcelable.Creator<Agent> CREATOR = new Parcelable.Creator<Agent>() {
        public Agent createFromParcel(Parcel in) {
            return new Agent(in);
        }

        public Agent[] newArray(int size) {
            return new Agent[size];
        }
    };


}
