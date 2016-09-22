package xyz.shaohui.sicilly.views.chat;

import javax.inject.Inject;
import javax.inject.Named;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import xyz.shaohui.sicilly.data.models.Message;
import xyz.shaohui.sicilly.data.models.User;
import xyz.shaohui.sicilly.data.network.api.MessageAPI;
import xyz.shaohui.sicilly.utils.ErrorUtils;
import xyz.shaohui.sicilly.views.chat.mvp.ChatPresenter;

/**
 * Created by shaohui on 16/9/23.
 */

public class ChatPresenterImpl extends ChatPresenter {

    MessageAPI mMessageService;

    User mOtherUser;

    @Inject
    ChatPresenterImpl(MessageAPI messageService, @Named("other_user")User otherUser) {
        mMessageService = messageService;
        mOtherUser = otherUser;
    }

    @Override
    public void fetchMessage() {
        mMessageService.messageList(mOtherUser.id(), 1)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(messages -> {
                    if (isViewAttached()) {
                        getView().showMessage(messages);
                    }
                }, throwable -> {
                    ErrorUtils.catchException(throwable);
                });
    }

    @Override
    public void fetchMessageNext(int page, Message lastMessage) {

    }
}
