package com.urjc.etsii.dlsi.pfc.stopp_spa.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.urjc.etsii.dlsi.pfc.stopp_spa.utilities.EncryptUtility;

/**
 * Created by vandia on 1/2/15.
 */
public class User implements Parcelable{

    public static final String PARCELABLE_NAME="mainUser";
    private long userID;
    private String name;
    private String password;
    private int age;
    private Agent agent;

    public User(String name, String password) throws Exception {
       this.name=name;
       this.password=(password==null?null:EncryptUtility.encrypt(password));
    }

    // example constructor that takes a Parcel and gives you an object populated with it's values
    private User (Parcel in) {
        userID = in.readLong();
        name = in.readString();
        password = in.readString();
        age = in.readInt();
        agent = (Agent) in.readValue(Agent.class.getClassLoader());
    }


    public long getUserID() {
        return userID;
    }

    public void setUserID(long userID) {
        this.userID = userID;
    }

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public Agent getAgent() {
        return agent;
    }

    public void setAgent(Agent agent) {
        this.agent = agent;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(userID);
        dest.writeString(name);
        dest.writeString(password);
        dest.writeInt(age);
        dest.writeValue(agent);
    }

    // this is used to regenerate your object. All Parcelables must have a CREATOR that implements these two methods
    public static final Parcelable.Creator<User> CREATOR = new Parcelable.Creator<User>() {
        public User createFromParcel(Parcel in) {
            return new User(in);

        }

        public User[] newArray(int size) {
            return new User[size];
        }
    };
}
