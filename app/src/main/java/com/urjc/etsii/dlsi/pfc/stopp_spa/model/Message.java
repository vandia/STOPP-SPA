package com.urjc.etsii.dlsi.pfc.stopp_spa.model;

import java.io.Serializable;

/**
 * Created by vandia on 4/3/15.
 */
public class Message implements Serializable{


    public enum Owner {
        ME,
        AGENT;

        @Override
        public String toString() {
            switch (this) {
                case ME:
                    return "ME";
                case AGENT:
                    return "AGENT";
                default:
                    return "";
            }
        }
    };

    private Owner owner;
    private String message;

    public Message(Owner owner, String message) {
        this.owner=owner;
        this.message=message;
    }


    public Owner getOwner() {
        return owner;
    }

    public void setOwner(Owner owner) {
        this.owner = owner;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }


}
