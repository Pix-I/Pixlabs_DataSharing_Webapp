package com.pixlabs.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by pix-i on 25/01/2017.
 * ${Copyright}
 */
public class GravatarUtil {



    private static String hex(byte[] array) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < array.length; ++i) {
            sb.append(Integer.toHexString((array[i]
                    & 0xFF) | 0x100).substring(1,3));
        }
        return sb.toString();
    }
    private static String md5Hex (String message) {
        try {
            MessageDigest md =
                    MessageDigest.getInstance("MD5");
            return hex (md.digest(message.getBytes("CP1252")));
        } catch (NoSuchAlgorithmException e) {
        } catch (UnsupportedEncodingException e) {

        }
        return null;
    }


    public static byte[] getGravatar(String email){
        byte[] image;
        email = email.toLowerCase();

        //  String url = gravatar.getUrl(userDto.getEmail().toLowerCase());
        final String url = "https://www.gravatar.com/avatar/" + md5Hex(email) + "?s=80&d=identicon&r=PG";
        try(InputStream in = new URL(url).openStream();
            ByteArrayOutputStream out = new ByteArrayOutputStream();){
            byte[] buffer = new byte[1024];
            int n;
            while (-1!=(n=in.read(buffer))){
                out.write(buffer,0,n);
            }
            return out.toByteArray();

        } catch (IOException e) {
            e.printStackTrace();
            //Should return a default image if an error occurs
        }
        System.out.println("Hmmm?");
        return null;
    }


}
