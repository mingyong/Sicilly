package xyz.shaohui.sicilly.push;

import android.content.Context;
import android.content.Intent;
import android.support.v4.util.Pair;
import android.text.TextUtils;
import android.util.Log;
import com.xiaomi.mipush.sdk.MiPushClient;
import com.xiaomi.mipush.sdk.MiPushCommandMessage;
import com.xiaomi.mipush.sdk.MiPushMessage;
import com.xiaomi.mipush.sdk.PushMessageReceiver;
import javax.inject.Inject;
import xyz.shaohui.sicilly.SicillyApplication;
import xyz.shaohui.sicilly.data.database.FeedbackDbAccessor;
import xyz.shaohui.sicilly.data.models.Feedback;
import xyz.shaohui.sicilly.data.network.auth.Text;
import xyz.shaohui.sicilly.views.feedback.FeedbackActivity;
import xyz.shaohui.sicilly.views.web.WebActivity;

/**
 * Created by shaohui on 16/9/25.
 */

public class MiBroadcastReceiver extends PushMessageReceiver {

    @Inject
    FeedbackDbAccessor mFeedbackDbAccessor;

    private static final String TYPE_WEB = "PUSH_WEB";

    private static final String TYPE_FEEDBACK = "PUSH_FEEDBACK";

    private static final String REGEX = " ";

    /**
     * 推送策略
     * 1. 推送web消息,直接打开webActivity,load URL
     * 2. 推送反馈回复,在数据库插入一条反馈, 点击打开feedbackActivity
     */

    @Override
    public void onNotificationMessageArrived(Context context, MiPushMessage miPushMessage) {
        PushComponent component = DaggerPushComponent.builder()
                .appComponent(SicillyApplication.getAppComponent())
                .build();
        component.inject(this);

        if (TextUtils.equals(miPushMessage.getContent().split(REGEX)[0], TYPE_FEEDBACK)) {
            Pair<String, String> pair = getMessageContent(miPushMessage.getContent());
            if (pair != null) {
                mFeedbackDbAccessor.insertFeedback(Feedback.receiveCreate(pair.second, pair.first));
            }
        }
    }

    @Override
    public void onReceivePassThroughMessage(Context context, MiPushMessage miPushMessage) {
    }

    @Override
    public void onNotificationMessageClicked(Context context, MiPushMessage miPushMessage) {
        // 统计点击
        MiPushClient.reportMessageClicked(context, miPushMessage);

        Log.i("TAG", miPushMessage.getContent());
        Intent intent;
        switch (miPushMessage.getContent().split(REGEX)[0]) {
            case TYPE_FEEDBACK:
                intent = new Intent(context, FeedbackActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
                break;
            case TYPE_WEB:
                Pair<String, String> pair = getMessageContent(miPushMessage.getContent());
                if (pair != null) {
                    intent = WebActivity.newIntent(context, pair.first);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                }
                break;
            default:
                Log.i("TAG", "点击" + miPushMessage.getContent().split(REGEX).length);
        }
    }

    @Override
    public void onCommandResult(Context context, MiPushCommandMessage miPushCommandMessage) {
        super.onCommandResult(context, miPushCommandMessage);
    }

    @Override
    public void onReceiveRegisterResult(Context context,
            MiPushCommandMessage miPushCommandMessage) {

    }

    private Pair<String, String> getMessageContent(String content) {
        String[] strs = content.split(REGEX);
        if (strs.length > 2) {
            return Pair.create(strs[1], strs[2]);
        } else if (strs.length > 1) {
            return Pair.create(strs[1], null);
        }
        return null;
    }

}
