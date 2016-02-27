package xyz.shaohui.sicilly.utils;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by kpt on 16/2/23.
 */
public class MyToast {

    private static Toast mToast;

    public static void showToast(Context context,String text){
        if (mToast!=null){
            mToast.setText(text);
            mToast.setDuration(Toast.LENGTH_SHORT);
        }
        else {
            mToast = Toast.makeText(context.getApplicationContext(),text,Toast.LENGTH_SHORT);
        }
        mToast.show();
    }

    public static void showToastLong(Context context,String text){
        if (mToast!=null){
            mToast.setText(text);
            mToast.setDuration(Toast.LENGTH_LONG);
        }
        else {
            mToast = Toast.makeText(context.getApplicationContext(),text,Toast.LENGTH_LONG);
        }
        mToast.show();
    }


}
