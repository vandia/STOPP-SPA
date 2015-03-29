package com.urjc.etsii.dlsi.pfc.stopp_spa.algorithm;

import android.content.Context;

import com.urjc.etsii.dlsi.pfc.stopp_spa.R;
import com.urjc.etsii.dlsi.pfc.stopp_spa.model.Word;

import org.xmlpull.v1.XmlPullParser;

import java.text.Collator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Locale;

/**
 * Created by vandia on 8/3/15.
 */
public class Inquiries {

    private HashMap<String,LinkedList<Word>> inquiries=new HashMap<String,LinkedList<Word>>();
    private final String TAG_INQUIRY ="inquiry";
    private final String SEPARATOR="\\|";

    private static Inquiries instance = null;
    private Context context;
    private Collator collator;

    private Inquiries(Context c){
        super();
        this.context=c.getApplicationContext();
        collator = Collator.getInstance(Locale.getDefault());
        collator.setStrength(Collator.PRIMARY);
    };

    private synchronized void initilize(){
        try {
            XmlPullParser xpp=context.getResources().getXml(R.xml.inquiries);
            while (xpp.getEventType()!=XmlPullParser.END_DOCUMENT) {
                if (xpp.getEventType()==XmlPullParser.START_TAG) {
                    if (xpp.getName().equals(TAG_INQUIRY)) {
                        String name=xpp.getAttributeValue(0);
                        String type=xpp.getAttributeValue(1);
                        xpp.next();
                        String text= xpp.getText();
                        if (inquiries.keySet().contains(name) && (inquiries.get(name)!=null)){
                            LinkedList<Word> aux=inquiries.get(name);
                            aux.addAll(cleanKeys(text, Word.WordType.parseWordType(type)));
                        }else {
                            inquiries.put(name, cleanKeys(text, Word.WordType.parseWordType(type)));
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

    public static synchronized Inquiries getInstance(Context context) {
        if (instance == null) {
            instance = new Inquiries(context);
            instance.initilize();
        }
        return instance;
    }

    private LinkedList<Word> cleanKeys(String keylist, Word.WordType type){
        if (keylist==null) return null;
        String[] inquiries= keylist.split(SEPARATOR);
        LinkedList<Word> result= new LinkedList<Word>();
        for (int i = 0; i < inquiries.length; i++) {
            result.add(new Word(collator.getCollationKey(inquiries[i]),type));
        }
        return result;
    }

    public LinkedList<Word> getInquiries(String phase){
        LinkedList<Word> result=new LinkedList<>();
        LinkedList<Word> aux=inquiries.get(phase);
        result.addAll(aux);
        return result;
    }
}
