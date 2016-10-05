package xyz.shaohui.sicilly.views.chat;

import java.util.Date;
import javax.inject.Inject;
import javax.inject.Named;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import xyz.shaohui.sicilly.SicillyApplication;
import xyz.shaohui.sicilly.data.models.Conversation;
import xyz.shaohui.sicilly.data.models.ConversationBean;
import xyz.shaohui.sicilly.data.models.Message;
import xyz.shaohui.sicilly.data.models.User;
import xyz.shaohui.sicilly.data.network.api.MessageAPI;
import xyz.shaohui.sicilly.utils.ErrorUtils;
import xyz.shaohui.sicilly.utils.RxUtils;
import xyz.shaohui.sicilly.views.chat.mvp.ChatPresenter;

/**
 * Created by shaohui on 16/9/23.
 */

public class ChatPresenterImpl extends ChatPresenter {

    MessageAPI mMessageService;

    User mOtherUser;

    @Inject
    ChatPresenterImpl(MessageAPI messageService, @Named("other_user") User otherUser) {
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
        mMessageService.messageList(mOtherUser.id(), page)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(messages -> {
                    if (isViewAttached()) {
                        if (messages.isEmpty()) {
                            getView().loadNoMore();
                        } else {
                            getView().showMoreMessage(messages);
                        }
                    }
                }, throwable -> {
                    ErrorUtils.catchException(throwable);
                    if (isViewAttached()) {
                        getView().loadMoreError();
                    }
                });
    }

    @Override
    public void sendMessage(String text) {
        RequestBody toId = RequestBody.create(MediaType.parse("text/plain"), mOtherUser.id());
        RequestBody messageText = RequestBody.create(MediaType.parse("text/plain"), text);
        mMessageService.sendMessage(toId, messageText)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnRequest(aLong -> {
                    if (isViewAttached()) {
                        getView().sendMessage(createLocalMessage(text));
                    }
                })
                .subscribe(message -> {
                    if (isViewAttached()) {
                        getView().sendMessage(message);
                    }
                }, throwable -> {
                    ErrorUtils.catchException(throwable);
                    if (isViewAttached()) {
                        getView().sendMessageFail(text);
                    }
                });
    }

    private Message createLocalMessage(String text) {
        Message message = new Message();
        message.setSender_id(SicillyApplication.currentUId());
        message.setText(text);
        message.setCreated_at(new Date());
        message.setIs_success(false);
        return message;
    }
}
