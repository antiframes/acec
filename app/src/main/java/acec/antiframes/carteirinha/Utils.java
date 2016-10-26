package acec.antiframes.carteirinha;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;


public class Utils {

    public static void saveAvatar(Context context,Bitmap bitmap){
        File imgFile= new File(context.getFilesDir(),"avatar.png");

        FileOutputStream out = null;
        try {
            out = new FileOutputStream(imgFile);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static Bitmap getAvatar(Context context){
        File imgFile= new File(context.getFilesDir(),"avatar.png");
        try {
            return BitmapFactory.decodeStream(new FileInputStream(imgFile));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String encryptString(String s) throws NoSuchAlgorithmException, InvalidKeySpecException, UnsupportedEncodingException, InvalidKeyException, NoSuchPaddingException, BadPaddingException, IllegalBlockSizeException {
        byte[] cleartext = s.getBytes("UTF8");
        byte[] encrypedPwd = Base64.encode(cleartext,Base64.DEFAULT);
        return new String(encrypedPwd);
    }

    public static String decodeString(String s) throws UnsupportedEncodingException {
        byte[] cleartext = s.getBytes("UTF8");
        byte[] encrypedPwdBytes = Base64.decode(cleartext,Base64.DEFAULT);
        return  new String(encrypedPwdBytes);
    }
}
