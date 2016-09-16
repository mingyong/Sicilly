package xyz.shaohui.sicilly.views.create_status.di;

import dagger.Component;
import xyz.shaohui.sicilly.app.di.AppComponent;
import xyz.shaohui.sicilly.data.network.di.StatusModule;
import xyz.shaohui.sicilly.views.create_status.CreateStatusActivity;
import xyz.shaohui.sicilly.views.create_status.CreateStatusFragment;
import xyz.shaohui.sicilly.views.create_status.mvp.CreateStatusPresenter;

/**
 * Created by shaohui on 16/9/11.
 */

@Component(
        dependencies = AppComponent.class,
        modules = {
                CreateStatusModule.class,
                StatusModule.class
        }

)
public interface CreateStatusComponent {

    void inject(CreateStatusActivity activity);

    void inject(CreateStatusFragment fragment);

    CreateStatusPresenter presenter();

}
