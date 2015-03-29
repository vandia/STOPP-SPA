package com.urjc.etsii.dlsi.pfc.stopp_spa.model;

/**
 * Created by vandia on 18/3/15.
 */
public class Movement {

    public static enum MovementType{
        LASTLOGIN(1),
        LASTCONVERSATION(2),
        DESERTION(3),
        COMPLETE(4);

        private int id;

        MovementType(int id){
            this.id=id;
        }

        public int getID (){
            return id;
        }
    }

    private MovementType type;
    private String description;

    public Movement(MovementType type, String description) {
        this.type = type;
        this.description = description;
    }

    public MovementType getType() {
        return type;
    }

    public void setType(MovementType type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
