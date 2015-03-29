package com.urjc.etsii.dlsi.pfc.stopp_spa.model;

import java.text.CollationKey;

/**
 * Created by vandia on 20/3/15.
 */
public class Word implements Comparable<Word> {

    public enum WordType{
        POSITIVE("POSITIVE"),
        NEGATIVE("NEGATIVE");

        String type;

        WordType(String type){
           this.type=type;
        }

        public static WordType parseWordType(String type){
            switch (type.toUpperCase()){
                case "POSITIVE": return POSITIVE;
                case "NEGATIVE": return NEGATIVE;
                default:return null;

            }
        }
    }


    private CollationKey key;
    private WordType type;

    public Word(CollationKey key, WordType type) {
        this.key = key;
        this.type = type;
    }

    public String getContent(){
        return key.getSourceString();
    }

    public WordType getType(){
        return type;
    }

    public CollationKey getKey() {
        return key;
    }


    @Override
    public boolean equals(Object o) {

        if (this==o) return true;
        if (! (o instanceof Word)) return false;
        Word that=(Word) o;

        if (this.key.compareTo(that.getKey())==0){
            return true;
        }

        return false;

    }

    @Override
    public int hashCode() {
        return key.hashCode();
    }

    @Override
    public int compareTo(Word another) {
        return this.key.compareTo(another.getKey());
    }
}
