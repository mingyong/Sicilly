package xyz.shaohui.sicilly.notification;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.text.TextUtils;

import xyz.shaohui.sicilly.R;
import xyz.shaohui.sicilly.data.SPDataManager;
import xyz.shaohui.sicilly.data.models.Message;
import xyz.shaohui.sicilly.data.models.Status;
import xyz.shaohui.sicilly.views.chat.ChatActivity;
import xyz.shaohui.sicilly.views.home.IndexActivity;
import xyz.shaohui.sicilly.views.status_detail.StatusDetailActivity;

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
                .setSmallIcon(R.mipmap.ic_message)
                .setLargeIcon(
                        BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_icon))
                .setContentIntent(pendingIntent);
        setNotiSound(context, builder);

        NotificationManager manager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(MESSAGE_ID, builder.build());
    }

    public static void showMentionNotice(Context context, String mentionStr, Status origin) {
        Intent intent = StatusDetailActivity.newIntent(context, origin, true);
        PendingIntent pendingIntent =
                PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        Notification.Builder builder = new Notification.Builder(context)
                .setContentTitle(context.getString(R.string.notification_mention_title))
                .setContentText(mentionStr)
                .setTicker(mentionStr)
                .setAutoCancel(true)
                .setSmallIcon(R.mipmap.ic_at)
                .setLargeIcon(BitmapFactory.decodeResource(context.getResources(),
                        R.mipmap.ic_icon))
                .setContentIntent(pendingIntent);
        setNotiSound(context, builder);

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
                .setContentIntent(pendingIntent);
        setNotiSound(context, builder);

        NotificationManager manager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(REQUEST_ID, builder.build());
    }

    private static void setNotiSound(Context context, Notification.Builder builder) {
        final String defaultSound = context.getString(R.string.setting_notification_sound_sicilly);
        String soundSetting = SPDataManager
                .getString(context.getString(R.string.setting_notification_sound_key),
                        defaultSound);
        if (TextUtils.equals(soundSetting, defaultSound)) {
            builder.setSound(Uri.parse("android.resource://xyz.shaohui.sicilly/raw/sound"));
        } else if (TextUtils.equals(soundSetting,
                context.getString(R.string.setting_notification_sound_default))) {
            Uri uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            builder.setSound(uri);
        } else {
            builder.setSound(null);
        }
    }

    public static void clearMessageNoti(Context context) {
        NotificationManager manager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        manager.cancel(MESSAGE_ID);
    }

    public static void clearMentionNoti(Context context) {
        NotificationManager manager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        manager.cancel(MENTION_ID);
    }

    public static void clearAll(Context context) {
        NotificationManager manager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        manager.cancelAll();
    }


}
