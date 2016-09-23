package xyz.shaohui.sicilly.data.network.di;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;
import rx.Observable;
import xyz.shaohui.sicilly.data.network.api.AccountAPI;

/**
 * Created by shaohui on 16/9/23.
 */

@Module
public class AccountModule {
    @Provides
    AccountAPI provideAccountService(Retrofit retrofit) {
        return retrofit.create(AccountAPI.class);
    }
}
