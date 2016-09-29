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
import xyz.shaohui.sicilly.SicillyApplication;
import xyz.shaohui.sicilly.data.database.AppUserDbAccessor;
import xyz.shaohui.sicilly.data.models.AppUser;
import xyz.shaohui.sicilly.data.models.FanNotification;
import xyz.shaohui.sicilly.data.network.api.AccountAPI;
import xyz.shaohui.sicilly.event.FriendRequestEvent;
import xyz.shaohui.sicilly.event.HomeMessageEvent;
import xyz.shaohui.sicilly.event.MentionEvent;
import xyz.shaohui.sicilly.event.MessageEvent;
import xyz.shaohui.sicilly.event.MessageSumEvent;
import xyz.shaohui.sicilly.service.di.DaggerSicillyServiceComponent;
import xyz.shaohui.sicilly.service.di.SicillyServiceComponent;
import xyz.shaohui.sicilly.views.home.IndexActivity;
import xyz.shaohui.sicilly.views.login.LoginActivity;

public class SicillyService extends Service {

    public static final int REPEAT_TIME = 10;

    @Inject
    AccountAPI mAccountService;

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

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        if (SicillyApplication.currentAppUser() == null) {
            AppUser user = intent.getParcelableExtra("user");
            if (user == null) {
                setupAppUser();
            } else {
                SicillyApplication.setCurrentAppUser(user);
            }
        }

        listenerNewMessage();

        return START_STICKY;
    }

    private void setupAppUser() {
        // 设置Application Token
        mAppUserDbAccessor.selectCurrentUser()
                .subscribe(cursor -> {
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
            
            if (notification.direct_messages() > 0) {
                mBus.post(new MessageEvent(notification.direct_messages()));
                sendMessageNotification();
            }

            if (notification.friend_requests() > 0) {
                mBus.post(new FriendRequestEvent(notification.friend_requests()));
                sendRequestNotification(notification.friend_requests());
            }

            if (notification.mentions() > 0) {
                mBus.post(new MentionEvent(notification.mentions()));
                sendMentionNotification(notification.mentions());
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

    }

    private void sendRequestNotification(int count) {

    }

    private void sendMentionNotification(int count) {

    }
}
