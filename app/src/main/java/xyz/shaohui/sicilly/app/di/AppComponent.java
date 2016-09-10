package xyz.shaohui.sicilly.app.di;

import dagger.Component;
import org.greenrobot.eventbus.EventBus;
import retrofit2.Retrofit;

/**
 * Created by shaohui on 16/9/10.
 */
@Component(
        modules = {
                AppModule.class
        })
public interface AppComponent {

    Retrofit getRetrofit();

    EventBus getBus();
}
