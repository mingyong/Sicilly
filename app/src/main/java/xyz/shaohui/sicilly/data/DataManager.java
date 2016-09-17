package xyz.shaohui.sicilly.data;

import android.content.Intent;
import org.greenrobot.eventbus.EventBus;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import xyz.shaohui.sicilly.SicillyApplication;
import xyz.shaohui.sicilly.data.models.Status;
import xyz.shaohui.sicilly.event.NewStatusEvent;
import xyz.shaohui.sicilly.notification.SendStatusNoti;
import xyz.shaohui.sicilly.provider.BusProvider;
import xyz.shaohui.sicilly.utils.ErrorUtils;

/**
 * Created by shaohui on 16/8/19.
 */
public class DataManager {

    public static void sendStatus(Observable<Status> observable, Intent intent) {
        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnRequest(time -> SendStatusNoti.sendNoti(SicillyApplication.getContext()))
                .subscribe(status -> {
                    SendStatusNoti.sendSuccessNoti(SicillyApplication.getContext());
                    EventBus bus = BusProvider.getBus();
                    bus.post(status);
                }, throwable -> {
                    SendStatusNoti.sendFailureNoti(intent);
                    ErrorUtils.catchException(throwable);
                });
    }
}
