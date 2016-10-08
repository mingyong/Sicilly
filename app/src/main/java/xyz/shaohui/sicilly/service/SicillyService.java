package xyz.shaohui.sicilly.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import java.util.concurrent.TimeUnit;
import javax.inject.Inject;
import org.greenrobot.eventbus.EventBus;
import rx.Observable;
import rx.functions.Action1;
import rx.schedulers.Schedulers;
import xyz.shaohui.sicilly.R;
import xyz.shaohui.sicilly.SicillyApplication;
import xyz.shaohui.sicilly.data.SPDataManager;
import xyz.shaohui.sicilly.data.database.AppUserDbAccessor;
import xyz.shaohui.sicilly.data.models.AppUser;
import xyz.shaohui.sicilly.data.models.FanNotification;
import xyz.shaohui.sicilly.data.models.Status;
import xyz.shaohui.sicilly.data.network.api.AccountAPI;
import xyz.shaohui.sicilly.data.network.api.MessageAPI;
import xyz.shaohui.sicilly.data.network.api.StatusAPI;
import xyz.shaohui.sicilly.event.FriendRequestEvent;
import xyz.shaohui.sicilly.event.HomeMessageEvent;
import xyz.shaohui.sicilly.event.MentionEvent;
import xyz.shaohui.sicilly.event.MessageEvent;
import xyz.shaohui.sicilly.event.MessageSumEvent;
import xyz.shaohui.sicilly.notification.NotificationUtils;
import xyz.shaohui.sicilly.service.di.DaggerSicillyServiceComponent;
import xyz.shaohui.sicilly.service.di.SicillyServiceComponent;
import xyz.shaohui.sicilly.utils.RxUtils;

public class SicillyService extends Service {

    public static final int REPEAT_TIME = 10;

    public static final int STATUS_YES = 1;
    public static final int STATUS_NO = 2;
    public static final int STATUS_UNCERTAIN = 0;

    private int canMessageNotice;

    private int canMentionNotice;

    private int canRequestNotice;

    private int sendMessageCount = 0;
    private int sendMentionCount = 0;
    private int sendRequestCount = 0;

    @Inject
    AccountAPI mAccountService;

    @Inject
    MessageAPI mMessageService;

    @Inject
    StatusAPI mStatusService;

    @Inject
    EventBus mBus;

    @Inject
    AppUserDbAccessor mAppUserDbAccessor;

    SicillyServiceComponent mComponent;

    public SicillyService() {
    }

    public static Intent newIntent(Context context, AppUser user) {
        Intent intent = new Intent(context, SicillyService.class);
        intent.putExtra("user", user);
        return intent;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mComponent = DaggerSicillyServiceComponent.builder()
                .appComponent(SicillyApplication.getAppComponent())
                .build();
        mComponent.inject(this);

        setupAppUser();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        listenerNewMessage();

        return START_STICKY;
    }

    private void setupAppUser() {
        // 设置Application Token
        mAppUserDbAccessor.selectCurrentUser().subscribe(cursor -> {
            if (cursor.getCount() > 0) {
                cursor.moveToFirst();
                SicillyApplication.setCurrentAppUser(AppUser.MAPPER.map(cursor));
            }
        });
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    private void listenerNewMessage() {
        Observable.defer(() -> mAccountService.notification())
                .subscribeOn(Schedulers.io())
                .delay(REPEAT_TIME, TimeUnit.SECONDS)
                .subscribe(handleNotification, throwable -> {
                    throwable.printStackTrace();
                    listenerNewMessage();
                });
    }

    private Action1<FanNotification> handleNotification = new Action1<FanNotification>() {

        @Override
        public void call(FanNotification notification) {

            if (notification.direct_messages() > sendMessageCount) {
                mBus.post(new MessageEvent(notification.direct_messages()));
                sendMessageNotification();
                sendMessageCount = notification.direct_messages();
            }

            if (notification.friend_requests() > sendRequestCount) {
                mBus.post(new FriendRequestEvent(notification.friend_requests()));
                sendRequestNotification(notification.friend_requests());
                sendRequestCount = notification.friend_requests();
            }

            if (notification.mentions() > sendMentionCount) {
                mBus.post(new MentionEvent(notification.mentions()));
                sendMentionNotification(notification.mentions());
                sendMentionCount = notification.mentions();
            }

            int sum = notification.mentions()
                    + notification.direct_messages()
                    + notification.friend_requests();

            int messageSum = notification.direct_messages() + notification.friend_requests();

            if (sum > 0) {
                mBus.post(new HomeMessageEvent(sum));
            }

            if (messageSum > 0) {
                mBus.post(new MessageSumEvent(messageSum));
            }

            listenerNewMessage();
        }
    };

    private void sendMessageNotification() {
        if (canMessageNotice == STATUS_UNCERTAIN) {
            canMessageNotice =
                    SPDataManager.loadSetting(this).sendMessageNotice() ? STATUS_YES : STATUS_NO;
        }
        if (canMessageNotice == STATUS_YES) {
            loadMessage();
        } else {
            sendMessageCount = 0;
        }
    }

    private void loadMessage() {
        mMessageService.inboxMessage(1).flatMap(messages -> {
            if (messages.isEmpty()) {
                return Observable.empty();
            } else {
                return Observable.just(messages.get(0));
            }
        }).subscribeOn(Schedulers.io()).subscribe(message -> {
            sendMessageCount = 0;
            NotificationUtils.showMessageNotice(this, message);
        }, RxUtils.ignoreNetError);
    }

    private void sendRequestNotification(int count) {

    }

    private void loadRequest() {

    }

    private void sendMentionNotification(int count) {
        if (canMentionNotice == STATUS_UNCERTAIN) {
            canMentionNotice =
                    SPDataManager.loadSetting(this).sendMentionNotice() ? STATUS_YES : STATUS_NO;
        }
        if (canMentionNotice == STATUS_YES) {
            loadMention(count);
        } else {
            sendMentionCount = 0;
        }
    }

    private void loadMention(int count) {
        mStatusService.mentionsStatus(count).flatMap(statuses -> {
            if (statuses.isEmpty()) {
                return Observable.empty();
            } else {
                StringBuilder names = new StringBuilder();
                for (Status status : statuses) {
                    names.append(status.user().screen_name());
                    if (statuses.indexOf(status) != statuses.size() - 1) {
                        names.append("、");
                    }
                }
                return Observable.just(String.format(getString(R.string.notification_new_mention),
                        names.toString()));
            }
        }).subscribeOn(Schedulers.io()).subscribe(string -> {
            sendMentionCount = 0;
            NotificationUtils.showMentionNotice(this, string);
        }, RxUtils.ignoreNetError);
    }
}
