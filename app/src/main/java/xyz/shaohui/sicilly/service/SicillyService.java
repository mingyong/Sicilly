package xyz.shaohui.sicilly.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Pair;
import java.util.concurrent.TimeUnit;
import javax.inject.Inject;
import rx.Observable;
import rx.Subscription;
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
import xyz.shaohui.sicilly.notification.NotificationUtils;
import xyz.shaohui.sicilly.service.aidl.IEventListener;
import xyz.shaohui.sicilly.service.aidl.ISicillyService;
import xyz.shaohui.sicilly.service.di.DaggerSicillyServiceComponent;
import xyz.shaohui.sicilly.service.di.SicillyServiceComponent;
import xyz.shaohui.sicilly.utils.RxUtils;

public class SicillyService extends Service {

    public static final int REPEAT_TIME = 10;

    public static final int STATUS_YES = 1;
    public static final int STATUS_NO = 2;
    public static final int STATUS_UNCERTAIN = 0;

    public static final int EVENT_TYPE_MESSAGE = 1;
    public static final int EVENT_TYPE_REQUEST = 2;
    public static final int EVENT_TYPE_MENTION = 3;

    private int canMessageNotice;

    private int canMentionNotice;

    private int canRequestNotice;

    @Inject
    AccountAPI mAccountService;

    @Inject
    MessageAPI mMessageService;

    @Inject
    StatusAPI mStatusService;

    @Inject
    AppUserDbAccessor mAppUserDbAccessor;

    SicillyServiceComponent mComponent;

    private long time;

    private Subscription mSubscription;

    private IEventListener mEventListener;

    public SicillyService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mComponent = DaggerSicillyServiceComponent.builder()
                .appComponent(SicillyApplication.getAppComponent())
                .build();
        mComponent.inject(this);

        time = REPEAT_TIME;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        // 设置Application Token
        mAppUserDbAccessor.selectCurrentUser().subscribe(cursor -> {
            if (cursor.getCount() > 0) {
                cursor.moveToFirst();
                SicillyApplication.setCurrentAppUser(AppUser.MAPPER.map(cursor));

                // 开始监听
                if (mSubscription == null || !mSubscription.isUnsubscribed()) {
                    listenerNewMessage();
                }
            }
        });

        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return new SicillyServiceImpl() {
            @Override
            public void registerListener(IEventListener listener) throws RemoteException {
                mEventListener = listener;
            }
        };
    }

    private void listenerNewMessage() {

        mSubscription = Observable.interval(time, TimeUnit.SECONDS)
                .first()
                .flatMap(aLong -> mAccountService.notification())
                .subscribeOn(Schedulers.io())
                .subscribe(handleNotification, Throwable::printStackTrace);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mSubscription.unsubscribe();
    }

    public abstract class SicillyServiceImpl extends ISicillyService.Stub {

        @Override
        public void basicTypes(int anInt, long aLong, boolean aBoolean, float aFloat,
                double aDouble, String aString) throws RemoteException {

        }
    }

    private Action1<FanNotification> handleNotification = new Action1<FanNotification>() {

        @Override
        public void call(FanNotification notification) {

            if (notification.direct_messages() > 0) {
                try {
                    mEventListener.onEvent(EVENT_TYPE_MESSAGE, notification.direct_messages());
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
                sendMessageNotification();
            }

            if (notification.friend_requests() > 0) {
                try {
                    mEventListener.onEvent(EVENT_TYPE_REQUEST, notification.friend_requests());
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
                sendRequestNotification(notification.friend_requests());
            }

            if (notification.mentions() > 0) {
                try {
                    mEventListener.onEvent(EVENT_TYPE_MENTION, notification.mentions());
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
                sendMentionNotification(notification.mentions());
            }

            listenerNewMessage();
        }
    };

    private void sendMessageNotification() {
        if (canMessageNotice == STATUS_UNCERTAIN) {
            canMessageNotice =
                    SPDataManager.loadAppSetting(this).sendMessageNotice() ? STATUS_YES : STATUS_NO;
        }
        loadMessage();
    }

    private void loadMessage() {
        mMessageService.inboxMessage(1).flatMap(messages -> {
            if (messages.isEmpty()) {
                return Observable.empty();
            } else {
                return Observable.just(messages.get(0));
            }
        }).subscribeOn(Schedulers.io()).subscribe(message -> {
            if (canMessageNotice == STATUS_YES) {
                NotificationUtils.showMessageNotice(this, message);
            }
        }, RxUtils.ignoreNetError);
    }

    private void sendRequestNotification(int count) {

    }

    private void loadRequest() {

    }

    private void sendMentionNotification(int count) {
        if (canMentionNotice == STATUS_UNCERTAIN) {
            canMentionNotice =
                    SPDataManager.loadAppSetting(this).sendMentionNotice() ? STATUS_YES : STATUS_NO;
        }
        loadMention(count);
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
                return Observable.just(Pair.create(String.format(getString(R.string.notification_new_mention),
                        names.toString()), statuses.get(statuses.size() - 1)));
            }
        }).subscribeOn(Schedulers.io()).subscribe(pair -> {
            if (canMentionNotice == STATUS_YES) {
                NotificationUtils.showMentionNotice(this, pair.first, pair.second);
            }
        }, RxUtils.ignoreNetError);
    }
}
