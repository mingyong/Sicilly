package xyz.shaohui.sicilly.views.feedback;

import com.google.gson.Gson;
import com.squareup.sqlbrite.BriteDatabase;
import java.io.File;
import javax.inject.Inject;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import org.greenrobot.eventbus.EventBus;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import xyz.shaohui.sicilly.R;
import xyz.shaohui.sicilly.SicillyApplication;
import xyz.shaohui.sicilly.SicillyFactory;
import xyz.shaohui.sicilly.data.database.FeedbackDbAccessor;
import xyz.shaohui.sicilly.data.models.AppUser;
import xyz.shaohui.sicilly.data.models.Feedback;
import xyz.shaohui.sicilly.data.network.api.SimpleAPI;
import xyz.shaohui.sicilly.leanCloud.service.RemoteService;
import xyz.shaohui.sicilly.utils.ErrorUtils;
import xyz.shaohui.sicilly.utils.RxUtils;
import xyz.shaohui.sicilly.views.feedback.mvp.FeedbackPresenter;
import xyz.shaohui.sicilly.views.feedback.upload.UploadImage;

/**
 * Created by shaohui on 16/9/24.
 */

public class FeedbackPresenterImpl extends FeedbackPresenter {

    private final EventBus mBus;

    private final FeedbackDbAccessor mFeedbackDbAccessor;

    private final SimpleAPI mSimpleService;

    @Inject
    FeedbackPresenterImpl(EventBus bus, FeedbackDbAccessor accessor, SimpleAPI simpleService) {
        mBus = bus;
        mFeedbackDbAccessor = accessor;

        mSimpleService = simpleService;
    }

    @Override
    public void loadFeedbackList() {
        mFeedbackDbAccessor.loadFeedback()
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext(feedbacks -> {
                    mFeedbackDbAccessor.makeFeedbackRead();
                })
                .subscribe(feedbacks -> {
                    if (isViewAttached()) {
                        if (feedbacks.isEmpty()) {
                            mFeedbackDbAccessor.insertFeedback(autoAnswer(true));
                        } else {
                            getView().showFeedback(feedbacks);
                        }
                    }
                }, throwable -> {
                    ErrorUtils.catchException(throwable);
                    if (isViewAttached()) {
                        getView().showFeedbackFail();
                    }
                });
    }

    @Override
    public void sendFeedback(String text) {
        Feedback feedback = Feedback.sendCreate(text, SicillyApplication.currentUId());
        mFeedbackDbAccessor.insertFeedback(feedback);
        mSimpleService.sendFeedback(SicillyFactory.FEEDBACK_URL, wrapperTextToSend(text))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(o -> {
                    BriteDatabase.Transaction transaction =
                            mFeedbackDbAccessor.mBriteDatabase.newTransaction();
                    mFeedbackDbAccessor.insertFeedback(feedback.sendSuccess());
                    mFeedbackDbAccessor.insertFeedback(autoAnswer(false));
                    transaction.markSuccessful();
                    transaction.end();
                }, RxUtils.ignoreNetError);

        // 发送RemoteFeedback
        AppUser user = SicillyApplication.currentAppUser();
        RemoteService.sendRemoteFeedback(user.id(), user.name(), SicillyApplication.getRegId(),
                text);
    }

    @Override
    public void uploadImage(String localPath) {
        File image = new File(localPath);
        String key = SicillyApplication.currentUId() + "@" + System.currentTimeMillis();
        String text = "图片上传中...";
        String url = SicillyFactory.FAN_STATIC + key;

        Feedback feedback = Feedback.sendImage(text, SicillyApplication.currentUId(), url);
        mFeedbackDbAccessor.insertFeedback(feedback);

        UploadImage.toQiniu(image, key, key1 -> {
            mSimpleService.sendFeedback(SicillyFactory.FEEDBACK_URL, wrapperTextToSend(url))
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(o -> {
                        BriteDatabase.Transaction transaction =
                                mFeedbackDbAccessor.mBriteDatabase.newTransaction();
                        mFeedbackDbAccessor.insertFeedback(
                                feedback.sendImageSuccess("发送成功！【暂不支持显示图片】"));
                        mFeedbackDbAccessor.insertFeedback(autoAnswer(false));
                        transaction.markSuccessful();
                        transaction.end();
                    }, RxUtils.ignoreNetError);
        });
    }

    @Override
    public void deleteAll() {
        mFeedbackDbAccessor.deleteAll();
    }

    private Feedback autoAnswer(boolean isFirst) {
        return Feedback.receiveCreate(SicillyApplication.getContext()
                        .getString(isFirst ? R.string.feedback_tip : R.string.feedback_auto_answer),
                SicillyApplication.currentUId());
    }

    private RequestBody wrapperTextToSend(String text) {
        AppUser user = SicillyApplication.currentAppUser();

        FeedbackSlack slack = new FeedbackSlack(text + "???" + SicillyApplication.getRegId(),
                String.format("%s(%s)", user.name(), user.id()), user.avatar());

        return RequestBody.create(MediaType.parse("text/plain"), new Gson().toJson(slack));
    }

    private class FeedbackSlack {
        String text;
        String username;
        String icon_url;

        FeedbackSlack(String text, String username, String icon_url) {
            this.text = text;
            this.username = username;
            this.icon_url = icon_url;
        }
    }
}
