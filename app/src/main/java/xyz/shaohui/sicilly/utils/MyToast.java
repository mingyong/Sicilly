package xyz.shaohui.sicilly.utils;

import android.content.Context;
import android.view.Gravity;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import xyz.shaohui.sicilly.R;

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

    public static void iconSuccess(Context context, String text) {
        Toast toast = Toast.makeText(context.getApplicationContext(), text, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);

        LinearLayout toastView = (LinearLayout) toast.getView();
        ImageView image = new ImageView(context);
        image.setImageResource(R.drawable.ic_alert_success);
        toastView.addView(image, 0);

        toast.show();
    }

    public static void iconFailure(Context context, String text) {
        Toast toast = Toast.makeText(context.getApplicationContext(), text, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);

        LinearLayout toastView = (LinearLayout) toast.getView();
        ImageView image = new ImageView(context);
        image.setImageResource(R.drawable.ic_alert_error);
        toastView.addView(image, 0);

        toast.show();
    }


}
