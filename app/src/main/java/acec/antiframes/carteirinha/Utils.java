package acec.antiframes.carteirinha;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

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
}
