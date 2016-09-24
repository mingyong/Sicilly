package xyz.shaohui.sicilly.views.feedback.di;

import dagger.Module;
import dagger.Provides;
import xyz.shaohui.sicilly.views.feedback.FeedbackPresenterImpl;
import xyz.shaohui.sicilly.views.feedback.mvp.FeedbackPresenter;

/**
 * Created by shaohui on 16/9/24.
 */

@Module
public class FeedbackModule {
    @Provides
    FeedbackPresenter provideFeedbackPresenter(FeedbackPresenterImpl presenter) {
        return presenter;
    }
}
