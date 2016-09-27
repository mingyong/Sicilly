package xyz.shaohui.sicilly.service;

import android.app.Service;
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
import xyz.shaohui.sicilly.data.models.FanNotification;
import xyz.shaohui.sicilly.data.network.api.AccountAPI;
import xyz.shaohui.sicilly.event.FriendRequestEvent;
import xyz.shaohui.sicilly.event.HomeMessageEvent;
import xyz.shaohui.sicilly.event.MentionEvent;
import xyz.shaohui.sicilly.event.MessageEvent;
import xyz.shaohui.sicilly.event.MessageSumEvent;
import xyz.shaohui.sicilly.service.di.DaggerSicillyServiceComponent;
import xyz.shaohui.sicilly.service.di.SicillyServiceComponent;

public class SicillyService extends Service {

    public static final int REPEAT_TIME = 10;

    @Inject
    AccountAPI mAccountService;

    @Inject
    EventBus mBus;

    SicillyServiceComponent mComponent;

    public SicillyService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mComponent = DaggerSicillyServiceComponent.builder()
                .appComponent(SicillyApplication.getAppComponent())
                .build();
        mComponent.inject(this);

        listenerNewMessage();
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

            Log.i("sicilly", "监听");

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
