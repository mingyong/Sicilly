package xyz.shaohui.sicilly.data.network.di;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;
import xyz.shaohui.sicilly.data.network.api.UserAPI;

/**
 * Created by shaohui on 16/9/18.
 */
@Module
public class UserModule {
    @Provides
    UserAPI provideUserService(Retrofit retrofit) {
        return retrofit.create(UserAPI.class);
    }
}
