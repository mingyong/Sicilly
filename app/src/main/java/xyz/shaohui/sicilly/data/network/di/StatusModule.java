package xyz.shaohui.sicilly.data.network.di;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;
import xyz.shaohui.sicilly.data.network.api.StatusAPI;

/**
 * Created by shaohui on 16/9/11.
 */

@Module
public class StatusModule {

    @Provides
    StatusAPI provideStatusService(Retrofit retrofit) {
        return retrofit.create(StatusAPI.class);
    }
}
