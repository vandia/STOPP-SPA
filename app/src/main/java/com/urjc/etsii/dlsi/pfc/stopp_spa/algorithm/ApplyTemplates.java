package com.urjc.etsii.dlsi.pfc.stopp_spa.algorithm;

import android.content.Context;

import com.urjc.etsii.dlsi.pfc.stopp_spa.R;

import org.xmlpull.v1.XmlPullParser;

import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by vandia on 8/3/15.
 */
public class ApplyTemplates {

    private HashMap<String,Pattern> patterns=new HashMap<String,Pattern>();
    private final String TAG_TEMPLATE="template";
    public final static String USER_PATTERN="user";
    public final static String FEELING_PATTERN="feeling";
    public final static String PROBLEM_PATTERN="problem";
    private static ApplyTemplates instance = null;
    private Context context;

    private ApplyTemplates(Context c){
        super();
        this.context=c.getApplicationContext();
    };

    private synchronized void initilize(){
        try {
            XmlPullParser xpp=context.getResources().getXml(R.xml.templates);
            while (xpp.getEventType()!=XmlPullParser.END_DOCUMENT) {
                if (xpp.getEventType()==XmlPullParser.START_TAG) {
                    if (xpp.getName().equals(TAG_TEMPLATE)) {
                        String name=xpp.getAttributeValue(0);
                        xpp.next();
                        String text= xpp.getText();
                        patterns.put(name, Pattern.compile(text));
                    }
                }
                xpp.next();
            }
        }
        catch (Throwable t) {
            t.printStackTrace();
        }

    }

    public static synchronized ApplyTemplates getInstance(Context context) {
        if (instance == null) {
            instance = new ApplyTemplates(context);
            instance.initilize();
        }
        return instance;
    }

    public String Substitution(String message, String templateType, String substitution){
        StringBuilder result=new StringBuilder(message);

        Pattern namePattern=patterns.get(templateType);
        Matcher m= namePattern.matcher(result);
        result.replace(0,result.length(),m.replaceAll(substitution));

        return result.toString();
    }

    public String Substitution(String message, HashMap<String,String> substitutions){
        StringBuilder result=new StringBuilder(message);

        for (String template : substitutions.keySet()) {
            Pattern namePattern=patterns.get(template);
            Matcher m= namePattern.matcher(result);
            result.replace(0,result.length(),m.replaceAll(substitutions.get(template)));

        }
        return result.toString();
    }

}
