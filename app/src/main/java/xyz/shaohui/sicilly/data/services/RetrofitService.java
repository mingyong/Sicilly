package xyz.shaohui.sicilly.data.services;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import xyz.shaohui.sicilly.data.services.api.StatusAPI;
import xyz.shaohui.sicilly.data.services.api.UserAPI;
import xyz.shaohui.sicilly.data.services.interceptors.SicillyInterceptor;

/**
 * Created by kpt on 16/2/22.
 */
public class RetrofitService {

    private StatusAPI statusService;
    private UserAPI userService;

    public RetrofitService() {
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(new SicillyInterceptor())
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://api.fanfou.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .client(client)
                .build();

        statusService = retrofit.create(StatusAPI.class);
        userService = retrofit.create(UserAPI.class);
    }

    public StatusAPI getStatusService() {
        return statusService;
    }

    public UserAPI getUserService() {
        return userService;
    }
}
