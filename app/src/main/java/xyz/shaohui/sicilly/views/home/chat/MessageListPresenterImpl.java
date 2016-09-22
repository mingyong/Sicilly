package xyz.shaohui.sicilly.views.home.chat;

import javax.inject.Inject;
import org.greenrobot.eventbus.EventBus;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import xyz.shaohui.sicilly.data.network.api.MessageAPI;
import xyz.shaohui.sicilly.views.home.chat.mvp.MessageListPresenter;

/**
 * Created by shaohui on 16/9/22.
 */

public class MessageListPresenterImpl extends MessageListPresenter {

    EventBus mBus;

    MessageAPI mMessageService;

    @Inject
    MessageListPresenterImpl(EventBus bus, MessageAPI messageService) {
        mBus = bus;
        mMessageService = messageService;
    }

    @Override
    public void fetchMessageList(int page) {
        mMessageService.conversationList(page)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(conversations -> {
                    if (isViewAttached()) {
                        if (page == 1) {
                            if (conversations.isEmpty()) {
                                getView().showEmpty();
                            } else {
                                getView().showConversation(conversations);
                            }
                        } else {
                            if (conversations.isEmpty()) {
                                getView().loadNoMore();
                            } else {
                                getView().showMoreConversation(conversations);
                            }
                        }
                    }
                }, throwable -> {
                    if (isViewAttached()) {
                        if (page == 1) {
                            getView().showNetError();
                        } else {
                            getView().loadMoreError();
                        }
                    }
                });
    }
}
