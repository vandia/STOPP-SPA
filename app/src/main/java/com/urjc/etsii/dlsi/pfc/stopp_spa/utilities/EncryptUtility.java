package com.urjc.etsii.dlsi.pfc.stopp_spa.utilities;

import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by vandia on 1/3/15.
 */
public class EncryptUtility {


    public static String encrypt(String plaintext) throws Exception
    {
        MessageDigest md = null;
        try
        {
            md = MessageDigest.getInstance("SHA");
        }
        catch(NoSuchAlgorithmException e)
        {
            throw new Exception(e.getMessage());
        }

        md.reset();
        md.update(plaintext.getBytes(Charset.forName("UTF-8")),0,plaintext.length());

        byte raw[] = md.digest();
        StringBuffer hexString = new StringBuffer();
        for (int i=0;i<raw.length;i++) {
            hexString.append(Integer.toHexString(0xFF & raw[i]));
        }

        return hexString.toString();
    }

}
