package xyz.shaohui.sicilly.data.services.notification;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.support.v7.app.NotificationCompat;

import xyz.shaohui.sicilly.R;
import xyz.shaohui.sicilly.utils.MyToast;

/**
 * Created by kpt on 16/3/19.
 */
public class CreateStatusNotification {

    public static void show(Context context){
        NotificationManager manager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context);

        mBuilder.setContentTitle("发送中")
                .setContentText("这是一条新的饭否信息")
                .setTicker("发送中..")
                .setWhen(System.currentTimeMillis())
                .setDefaults(Notification.DEFAULT_VIBRATE);

        mBuilder.setSmallIcon(R.drawable.ic_alert_error);

        Notification notification = mBuilder.build();
        notification.flags = Notification.FLAG_AUTO_CANCEL;

        manager.notify(1, notification);
    }

    public static void update() {

    }

}
