package xyz.shaohui.sicilly.utils;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by shaohui on 16/10/5.
 */

public class HashUtils {

    public static String forMD5(String target) {
        try {
            byte[] messageDigest = MessageDigest.getInstance("MD5").digest(target.getBytes());

            StringBuilder hexString = new StringBuilder();
            for (int i=0; i<messageDigest.length; i++)
                hexString.append(Integer.toHexString(0xFF & messageDigest[i]));
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }

}
