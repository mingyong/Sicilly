package xyz.shaohui.sicilly.app.di;

import android.content.Context;
import android.support.annotation.Nullable;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.squareup.sqlbrite.BriteDatabase;
import com.squareup.sqlbrite.SqlBrite;
import dagger.Module;
import dagger.Provides;
import java.util.concurrent.TimeUnit;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import org.greenrobot.eventbus.EventBus;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.schedulers.Schedulers;
import xyz.shaohui.sicilly.SicillyApplication;
import xyz.shaohui.sicilly.data.database.DbHelper;
import xyz.shaohui.sicilly.data.network.MyAdapterFactory;
import xyz.shaohui.sicilly.data.network.RetrofitService;
import xyz.shaohui.sicilly.data.network.okHttp.OkHttpInterceptor;
import xyz.shaohui.sicilly.provider.BusProvider;

/**
 * Created by shaohui on 16/9/10.
 */

@Module
public class AppModule {

    @Provides
    Gson provideGson() {
        return new GsonBuilder().setDateFormat("EE MMM dd HH:mm:ss Z yyyy")
                .registerTypeAdapterFactory(MyAdapterFactory.create())
                .create();
    }

    @Provides
    OkHttpClient provideHttpClient() {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        return new OkHttpClient.Builder().connectTimeout(20, TimeUnit.SECONDS)  // 超时20s
                .addInterceptor(interceptor)
                .addInterceptor(new OkHttpInterceptor())
                .retryOnConnectionFailure(true)
                .build();
    }

    @Provides
    Retrofit provideRetrofit(Gson gson, OkHttpClient client) {
        return new Retrofit.Builder().baseUrl("http://api.fanfou.com/")
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(client)
                .build();
    }

    @Provides
    EventBus providerBus() {
        return BusProvider.getBus();
    }

    @Provides
    Context provideContext() {
        return SicillyApplication.getContext();
    }

    @Provides
    DbHelper prodiveDbHelper(Context context) {
        return new DbHelper(context);
    }

    @Provides
    BriteDatabase provideBriteDatabase(DbHelper dbHelper) {
        SqlBrite sqlBrite = SqlBrite.create();
        return sqlBrite.wrapDatabaseHelper(dbHelper, Schedulers.io());
    }
}
