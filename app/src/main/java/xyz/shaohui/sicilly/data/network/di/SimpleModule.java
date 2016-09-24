package xyz.shaohui.sicilly.data.network.di;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;
import xyz.shaohui.sicilly.data.network.api.SimpleAPI;

/**
 * Created by shaohui on 16/9/24.
 */

@Module
public class SimpleModule {

    @Provides
    SimpleAPI provideSimpleService(Retrofit retrofit) {
        return retrofit.create(SimpleAPI.class);
    }

}
