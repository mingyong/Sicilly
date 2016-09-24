package xyz.shaohui.sicilly.views.feedback.di;

import dagger.Component;
import xyz.shaohui.sicilly.app.di.AppComponent;
import xyz.shaohui.sicilly.data.database.di.FeedbackDbModule;
import xyz.shaohui.sicilly.data.network.di.SimpleModule;
import xyz.shaohui.sicilly.views.feedback.FeedbackActivity;
import xyz.shaohui.sicilly.views.feedback.FeedbackFragment;
import xyz.shaohui.sicilly.views.feedback.mvp.FeedbackPresenter;

/**
 * Created by shaohui on 16/9/24.
 */

@Component(dependencies = AppComponent.class,
        modules = {FeedbackModule.class, SimpleModule.class})
public interface FeedbackComponent {

    void inject(FeedbackActivity activity);

    void inject(FeedbackFragment fragment);

    FeedbackPresenter presenter();

}
