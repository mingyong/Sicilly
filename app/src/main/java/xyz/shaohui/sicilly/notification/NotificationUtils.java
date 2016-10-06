package xyz.shaohui.sicilly.notification;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import xyz.shaohui.sicilly.R;
import xyz.shaohui.sicilly.data.models.Message;
import xyz.shaohui.sicilly.views.chat.ChatActivity;
import xyz.shaohui.sicilly.views.home.IndexActivity;

/**
 * Created by shaohui on 16/9/27.
 */

public class NotificationUtils {

    public static final int MESSAGE_ID = 1;

    public static final int MENTION_ID = 2;

    public static final int REQUEST_ID = 3;

    public static void showMessageNotice(Context context, Message message) {

        Intent resultIntent = ChatActivity.newIntent(context, message.getSender());
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        stackBuilder.addParentStack(ChatActivity.class);
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent pendingIntent =
                stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

        Notification.Builder builder = new Notification.Builder(context).setAutoCancel(true)
                .setTicker(message.getText())
                .setContentTitle(message.getSender_screen_name())
                .setContentText(message.getText())
                .setSmallIcon(R.drawable.ic_message)
                .setLargeIcon(
                        BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_icon))
                .setContentIntent(pendingIntent)
                .setSound(Uri.parse("android.resource://xyz.shaohui.sicilly/raw/sound"));

        NotificationManager manager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(MESSAGE_ID, builder.build());
    }

    public static void showMentionNotice(Context context, String mentionStr) {
        Intent intent = IndexActivity.newIntent(context, IndexActivity.ACTION_MENTION);
        PendingIntent pendingIntent =
                PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        Notification.Builder builder = new Notification.Builder(context)
                .setContentTitle(context.getString(R.string.notification_mention_title))
                .setContentText(mentionStr)
                .setTicker(mentionStr)
                .setAutoCancel(true)
                .setSmallIcon(R.drawable.ic_at_black)
                .setLargeIcon(BitmapFactory.decodeResource(context.getResources(),
                        R.drawable.ic_at_black))
                .setContentIntent(pendingIntent)
                .setSound(Uri.parse("android.resource://xyz.shaohui.sicilly/raw/sound"));

        NotificationManager manager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(MENTION_ID, builder.build());
    }

    public static void showRequestNotice(Context context, String request) {
        Intent intent = IndexActivity.newIntent(context, IndexActivity.ACTION_MENTION);
        PendingIntent pendingIntent =
                PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        Notification.Builder builder = new Notification.Builder(context)
                .setContentTitle(context.getString(R.string.notification_new_request))
                .setContentText(request)
                .setTicker(context.getString(R.string.notification_new_request))
                .setAutoCancel(true)
                .setSmallIcon(R.drawable.ic_follow)
                .setLargeIcon(BitmapFactory.decodeResource(context.getResources(),
                        R.drawable.ic_follow))
                .setContentIntent(pendingIntent)
                .setSound(Uri.parse("android.resource://xyz.shaohui.sicilly/raw/sound"));

        NotificationManager manager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(REQUEST_ID, builder.build());
    }


}
