package xyz.shaohui.sicilly.data;

import javax.inject.Inject;
import rx.Observable;
import rx.schedulers.Schedulers;
import xyz.shaohui.sicilly.data.models.Message;
import xyz.shaohui.sicilly.data.network.api.MessageAPI;
import xyz.shaohui.sicilly.utils.RxUtils;

/**
 * Created by shaohui on 16/9/29.
 */

public class MessageManager {

    @Inject
    MessageAPI mMessageService;

    public Observable<Message> fetchNewMessage() {
        return mMessageService.inboxMessage(1)
                .subscribeOn(Schedulers.io())
                .map(messages -> messages.get(0));
    }

}
