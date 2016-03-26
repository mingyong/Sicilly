package xyz.shaohui.sicilly.data.services;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import xyz.shaohui.sicilly.data.services.api.FriendshipAPI;
import xyz.shaohui.sicilly.data.services.api.MessageAPI;
import xyz.shaohui.sicilly.data.services.api.SearchAPI;
import xyz.shaohui.sicilly.data.services.api.StatusAPI;
import xyz.shaohui.sicilly.data.services.api.UserAPI;
import xyz.shaohui.sicilly.data.services.interceptors.SicillyInterceptor;

/**
 * Created by kpt on 16/2/22.
 */
public class RetrofitService {

    private StatusAPI statusService;
    private UserAPI userService;
    private MessageAPI messageService;
    private SearchAPI searchService;
    private FriendshipAPI friendshipService;

    public RetrofitService() {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(interceptor)
                .addNetworkInterceptor(new SicillyInterceptor())
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://api.fanfou.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .client(client)
                .build();

        statusService = retrofit.create(StatusAPI.class);
        userService = retrofit.create(UserAPI.class);
        messageService = retrofit.create(MessageAPI.class);
        searchService = retrofit.create(SearchAPI.class);
        friendshipService = retrofit.create(FriendshipAPI.class);

    }

    public StatusAPI getStatusService() {
        return statusService;
    }

    public UserAPI getUserService() {
        return userService;
    }

    public MessageAPI getMessageService() {
        return messageService;
    }

    public SearchAPI getSearchService() {
        return searchService;
    }

    public FriendshipAPI getFriendshipService() {
        return friendshipService;
    }
}
