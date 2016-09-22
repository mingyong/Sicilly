package xyz.shaohui.sicilly.data.network.di;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;
import xyz.shaohui.sicilly.data.network.api.FriendshipAPI;

/**
 * Created by shaohui on 16/9/21.
 */

@Module
public class FriendshipModule {
    @Provides
    FriendshipAPI provideFriendship(Retrofit retrofit) {
        return retrofit.create(FriendshipAPI.class);
    }
}
