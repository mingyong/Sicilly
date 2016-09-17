package xyz.shaohui.sicilly.notification;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import xyz.shaohui.sicilly.R;
import xyz.shaohui.sicilly.SicillyApplication;
import xyz.shaohui.sicilly.data.models.Status;
import xyz.shaohui.sicilly.views.create_status.CreateStatusActivity;

/**
 * Created by shaohui on 16/9/17.
 */

public class SendStatusNoti {

    private final static int SEND_NOTI_ID = 1;
    private final static int SUCCESS_NOTI_ID = 2;
    private final static int FAILURE_NOTI_ID = 3;

    public static void sendNoti(Context context) {
        Notification.Builder builder =
                new Notification.Builder(context).setSmallIcon(R.drawable.ic_upload)
                        .setContentTitle(context.getString(R.string.send_noti_title))
                        .setTicker(context.getString(R.string.send_noti_title))
                        .setAutoCancel(true);

        Notification notification = builder.build();
        //notification.flags = Notification.FLAG_AUTO_CANCEL;
        NotificationManager manager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        //manager.notify(SEND_NOTI_ID, notification);
        //manager.cancel(SEND_NOTI_ID);
    }

    public static void sendSuccessNoti(Context context) {
        NotificationManager manager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        Notification notification = new Notification.Builder(context).setContentTitle(
                context.getString(R.string.send_status_success))
                .setSmallIcon(R.drawable.ic_success)
                .setTicker(context.getString(R.string.send_status_success))
                .build();

        manager.notify(SUCCESS_NOTI_ID, notification);
        manager.cancel(SUCCESS_NOTI_ID);
    }

    public static void sendFailureNoti(Intent intent) {
        Context context = SicillyApplication.getContext();
        PendingIntent pendingIntent =
                PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_ONE_SHOT);
        Notification notification =
                new Notification.Builder(context).setSmallIcon(R.drawable.ic_failure)
                        .setTicker(context.getString(R.string.send_status_failure))
                        .setContentTitle(context.getString(R.string.send_status_failure))
                        .setAutoCancel(true)
                        .setContentIntent(pendingIntent)
                        .build();

        NotificationManager manager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(FAILURE_NOTI_ID, notification);
    }
}
