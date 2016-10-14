package xyz.shaohui.sicilly.data.appModel;

import com.google.auto.value.AutoValue;

/**
 * Created by shaohui on 16/9/29.
 */

@AutoValue
public abstract class AppSetting {

    public static final String SEND_MESSAGE_KEY = "send_message";

    public static final String SEND_MENTION_KEY = "send_mention";

    public static final String SEND_REQUEST_KEY = "send_request";

    public static final String SEND_NOTIFICATION_SOUND = "notification_sound";

    public static AppSetting create(boolean sendMessageNotice, boolean sendMentionNotice,
            boolean sendRequestNotice) {
        return new AutoValue_AppSetting(sendMessageNotice, sendMentionNotice, sendRequestNotice);
    }

    public abstract boolean sendMessageNotice();

    public abstract boolean sendMentionNotice();

    public abstract boolean sendRequestNotice();

}
