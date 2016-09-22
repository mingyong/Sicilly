package xyz.shaohui.sicilly.app.di;

import android.content.Context;
import dagger.Component;
import org.greenrobot.eventbus.EventBus;
import retrofit2.Retrofit;
import xyz.shaohui.sicilly.data.database.AppUserDbAccessor;
import xyz.shaohui.sicilly.data.database.DbHelper;
import xyz.shaohui.sicilly.views.login.LoginActivity;
import xyz.shaohui.sicilly.views.activities.SplashActivity;

/**
 * Created by shaohui on 16/9/10.
 */
@Component(
        modules = {
                AppModule.class
        })
public interface AppComponent {

    void inject(SplashActivity activity);

    Retrofit getRetrofit();

    EventBus getBus();

    Context getContext();

    DbHelper getDbHelper();

    AppUserDbAccessor getAppUserDbAccessor();
}
