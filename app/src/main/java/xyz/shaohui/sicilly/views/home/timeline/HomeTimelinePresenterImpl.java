package xyz.shaohui.sicilly.views.home.timeline;

import javax.inject.Inject;
import org.greenrobot.eventbus.EventBus;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import xyz.shaohui.sicilly.SicillyApplication;
import xyz.shaohui.sicilly.data.models.Status;
import xyz.shaohui.sicilly.data.network.api.StatusAPI;
import xyz.shaohui.sicilly.views.home.timeline.mvp.HomeTimelinePresenter;

/**
 * Created by shaohui on 16/9/10.
 */

public class HomeTimelinePresenterImpl extends HomeTimelinePresenter {

    private EventBus mBus;

    private StatusAPI statusService;

    @Inject
    HomeTimelinePresenterImpl(StatusAPI statusService, EventBus enentBus) {
        this.statusService = statusService;
        mBus = enentBus;
    }

    @Override
    public void loadMessage() {
        statusService.homeStatus()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(statuses -> {
                    if (isViewAttached()) {
                        getView().showMessage(statuses);
                    }
                }, throwable -> {
                    if (isViewAttached()) {
                        getView().networkError();
                    }
                    throwable.printStackTrace();
                });
    }

    @Override
    public void loadMoreMessage(int page, Status status) {
        statusService.homeStatusNext(page)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(statuses -> {
                    if (isViewAttached()) {
                        getView().showMoreMessage(statuses);
                    }
                });
    }

    @Override
    public void listenerNewMessage() {

    }

    @Override
    public void starMessage(String messageId) {

    }

    @Override
    public void deleteMessage(String messageId) {

    }

    @Override
    public void commmentMessage(String messageId) {

    }

    @Override
    public void repostMessage(String messageId) {

    }
}
