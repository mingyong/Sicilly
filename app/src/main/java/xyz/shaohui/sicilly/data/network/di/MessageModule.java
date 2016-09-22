package xyz.shaohui.sicilly.data.network.di;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;
import xyz.shaohui.sicilly.data.network.api.MessageAPI;

/**
 * Created by shaohui on 16/9/22.
 */

@Module
public class MessageModule {
    @Provides
    MessageAPI provideMessgeService(Retrofit retrofit) {
        return retrofit.create(MessageAPI.class);
    }
}
