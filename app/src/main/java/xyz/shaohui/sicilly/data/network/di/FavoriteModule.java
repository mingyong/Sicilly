package xyz.shaohui.sicilly.data.network.di;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;
import xyz.shaohui.sicilly.data.network.api.FavoriteAPI;

/**
 * Created by shaohui on 16/9/16.
 */

@Module
public class FavoriteModule {
    @Provides
    FavoriteAPI provideFavoriteService(Retrofit retrofit) {
        return retrofit.create(FavoriteAPI.class);
    }


}
