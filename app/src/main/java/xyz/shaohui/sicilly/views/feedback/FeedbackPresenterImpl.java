package xyz.shaohui.sicilly.views.feedback;

import android.util.Log;
import com.squareup.sqlbrite.BriteDatabase;
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
import xyz.shaohui.sicilly.data.models.Feedback;
import xyz.shaohui.sicilly.data.network.api.SimpleAPI;
import xyz.shaohui.sicilly.utils.ErrorUtils;
import xyz.shaohui.sicilly.utils.RxUtils;
import xyz.shaohui.sicilly.views.feedback.mvp.FeedbackPresenter;

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
                .subscribe(feedbacks -> {
                    if (isViewAttached()) {
                        Log.i("TAG", feedbacks.size() + "");
                        if (feedbacks.isEmpty()) {
                            Log.i("TAG", "kong");
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
        RequestBody body = RequestBody.create(MediaType.parse("text/plain"),
                String.format("{\"text\":\"%s\"}", text));
        //return String.format("{\"text\":\"%s\"}", text);
        return body;
    }
}
