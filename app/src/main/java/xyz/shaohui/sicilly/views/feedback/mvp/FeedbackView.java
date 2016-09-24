package xyz.shaohui.sicilly.views.feedback.mvp;

import com.hannesdorfmann.mosby.mvp.MvpView;
import java.util.List;
import xyz.shaohui.sicilly.data.models.Feedback;

/**
 * Created by shaohui on 16/9/24.
 */

public interface FeedbackView extends MvpView {

    void showFeedback(List<Feedback> feedbacks);

    void showFeedbackFail();

    void sendFeedbackFail(String text);
}
