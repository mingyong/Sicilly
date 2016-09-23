package xyz.shaohui.sicilly.views.home.chat;

import android.util.Log;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;
import org.greenrobot.eventbus.EventBus;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func2;
import rx.schedulers.Schedulers;
import xyz.shaohui.sicilly.data.models.Conversation;
import xyz.shaohui.sicilly.data.models.ConversationBean;
import xyz.shaohui.sicilly.data.network.api.AccountAPI;
import xyz.shaohui.sicilly.data.network.api.MessageAPI;
import xyz.shaohui.sicilly.event.MentionEvent;
import xyz.shaohui.sicilly.event.MessageEvent;
import xyz.shaohui.sicilly.utils.ErrorUtils;
import xyz.shaohui.sicilly.views.home.chat.mvp.MessageListPresenter;

/**
 * Created by shaohui on 16/9/22.
 */

public class MessageListPresenterImpl extends MessageListPresenter {

    private final EventBus mBus;

    private final MessageAPI mMessageService;

    private final AccountAPI mAccountService;

    @Inject
    MessageListPresenterImpl(EventBus bus, MessageAPI messageService, AccountAPI accountService) {
        mBus = bus;
        mMessageService = messageService;
        mAccountService = accountService;
    }

    @Override
    public void fetchMessageList() {
        Observable<Conversation> mainObservable = mMessageService.conversationList(1)
                .flatMap(Observable::from);

        Observable<ConversationBean> notificationObservable =
                mAccountService.notification().flatMap(fanNotification -> {
                    if (fanNotification.mentions() > 0) {
                        mBus.post(new MentionEvent(fanNotification.mentions()));
                    }

                    if (fanNotification.direct_messages() > 0
                            || fanNotification.friend_requests() > 0) {
                        mBus.post(new MessageEvent(fanNotification.direct_messages()
                                + fanNotification.friend_requests()));
                    }

                    if (fanNotification.friend_requests() > 0) {
                        return Observable.just(
                                new ConversationBean(fanNotification.friend_requests()));
                    }
                    return Observable.empty();
                });

        Observable.concat(notificationObservable, mainObservable)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .toList()
                .subscribe(conversationBeen -> {
                    if (isViewAttached()) {
                        if (conversationBeen.isEmpty()) {
                            getView().showEmpty();
                        } else {
                            getView().showConversation(conversationBeen);
                        }
                    }
                }, throwable -> {
                    if (isViewAttached()) {
                        getView().showNetError();
                    }
                });



        //mainObservable.zipWith(notificationObservable, new Func2<List<Conversation>, ConversationBean, List<ConversationBean>>() {
        //
        //    @Override
        //    public List<ConversationBean> call(List<Conversation> conversations,
        //            ConversationBean conversationBean) {
        //        List<ConversationBean> result = new ArrayList<>();
        //        result.addAll(conversations);
        //        if (conversationBean != null) {
        //            result.add(0, conversationBean);
        //        }
        //        Log.i("TAG", "result.size:" + result.size());
        //        return result;
        //    }
        //})
        ////mMessageService.conversationList(1)
        //        .subscribeOn(Schedulers.io())
        //        .observeOn(AndroidSchedulers.mainThread())
        //        .subscribe(conversations -> {
        //            Log.i("TAG", conversations.size() + "测试");
        //            if (isViewAttached()) {
        //                if (conversations.isEmpty()) {
        //                    getView().showEmpty();
        //                } else {
        //                    getView().showConversation(conversations);
        //                }
        //            }
        //        },  throwable -> {
        //            ErrorUtils.catchException(throwable);
        //            if (isViewAttached()) {
        //                getView().showNetError();
        //            }
        //        });
    }

    @Override
    public void fetchMessageListNext(int page) {
        mMessageService.conversationList(page)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(conversations -> {
                    if (isViewAttached()) {
                        if (conversations.isEmpty()) {
                            getView().loadNoMore();
                        } else {
                            getView().showMoreConversation(conversations);
                        }
                    }
                }, throwable -> {
                    ErrorUtils.catchException(throwable);
                    if (isViewAttached()) {
                        getView().loadMoreError();
                    }
                });
    }
}
