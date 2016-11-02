package xyz.shaohui.sicilly.data.network.di;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;
import xyz.shaohui.sicilly.data.network.api.SearchAPI;
import xyz.shaohui.sicilly.data.network.api.StatusAPI;

/**
 * Created by shaohui on 2016/11/1.
 */

@Module
public class SearchModule {

    @Provides
    SearchAPI provideSearch(Retrofit retrofit) {
        return retrofit.create(SearchAPI.class);
    }

}
