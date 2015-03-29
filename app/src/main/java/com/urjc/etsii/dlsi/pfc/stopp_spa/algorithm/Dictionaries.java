package com.urjc.etsii.dlsi.pfc.stopp_spa.algorithm;

import android.content.Context;

import com.urjc.etsii.dlsi.pfc.stopp_spa.R;
import com.urjc.etsii.dlsi.pfc.stopp_spa.model.Word;

import org.xmlpull.v1.XmlPullParser;

import java.text.Collator;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

/**
 * Created by vandia on 15/3/15.
 */
public class Dictionaries {
    private HashMap<String,ArrayList<Word>> dictionaries = new HashMap<>();
    private final String TAG_KEYS ="dictionary";
    private final String SEPARATOR="\\|";
    public static final String ANSWER="ANSWER";

    private Context context;
    private Collator collator;

    //instance of the singletone
    private static Dictionaries instance = null;

    private Dictionaries(Context c){
        super();
        this.context=c.getApplicationContext();
        collator = Collator.getInstance(Locale.getDefault());
        collator.setStrength(Collator.PRIMARY);
    };

    private synchronized void initilize(){
        try {
            XmlPullParser xpp=context.getResources().getXml(R.xml.dictionaries);
            while (xpp.getEventType()!=XmlPullParser.END_DOCUMENT) {
                if (xpp.getEventType()==XmlPullParser.START_TAG) {
                    if (xpp.getName().equals(TAG_KEYS)) {
                        String name=xpp.getAttributeValue(0);
                        String type=xpp.getAttributeValue(1);
                        xpp.next();
                        String text= xpp.getText();
                        if (dictionaries.keySet().contains(name)){
                            ArrayList<Word> aux=dictionaries.get(name);
                            aux.addAll(cleanKeys(text, Word.WordType.parseWordType(type)));
                        }else {
                            dictionaries.put(name, cleanKeys(text, Word.WordType.parseWordType(type)));
                        }

                    }
                }
                xpp.next();
            }
        }
        catch (Throwable t) {
            t.printStackTrace();
        }

    }

    public static synchronized Dictionaries getInstance(Context context) {
        if (instance == null) {
            instance = new Dictionaries(context);
            instance.initilize();
        }
        return instance;
    }



    private ArrayList<Word> cleanKeys(String keylist, Word.WordType type){
        String[] dictionary= keylist.split(SEPARATOR);
        ArrayList<Word> result= new ArrayList<Word>();
        for (int i = 0; i < dictionary.length; i++) {
            result.add(new Word(collator.getCollationKey(dictionary[i]),type));
        }
        return result;
    }

    public ArrayList<Word> getDictionary(String phase){
        return dictionaries.get(phase);
    }

    public Collator getCollator() {
        return collator;
    }
}
