package xyz.shaohui.sicilly.data.network;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import xyz.shaohui.sicilly.R;
import xyz.shaohui.sicilly.data.network.api.AccountAPI;
import xyz.shaohui.sicilly.data.network.api.BlockAPI;
import xyz.shaohui.sicilly.data.network.api.FavoriteAPI;
import xyz.shaohui.sicilly.data.network.api.FriendshipAPI;
import xyz.shaohui.sicilly.data.network.api.MessageAPI;
import xyz.shaohui.sicilly.data.network.api.SearchAPI;
import xyz.shaohui.sicilly.data.network.api.StatusAPI;
import xyz.shaohui.sicilly.data.network.api.TrendAPI;
import xyz.shaohui.sicilly.data.network.api.UserAPI;
import xyz.shaohui.sicilly.data.network.okHttp.OkHttpInterceptor;

/**
 * Created by shaohui on 16/8/4.
 */
public class RetrofitService {

    private AccountAPI accountService;
    private BlockAPI blockService;
    private FavoriteAPI favoriteService;
    private FriendshipAPI friendshipService;
    private MessageAPI messageService;
    private SearchAPI searchService;
    private StatusAPI statusService;
    private TrendAPI trendService;
    private UserAPI userService;

    public RetrofitService() {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(20, TimeUnit.SECONDS)  // 超时20s
                .addInterceptor(interceptor)
                .addInterceptor(new OkHttpInterceptor())
                .retryOnConnectionFailure(true)
                .build();

        Gson gson = new GsonBuilder()
                .setDateFormat("EE MMM dd HH:mm:ss Z yyyy")
                .registerTypeAdapterFactory(MyAdapterFactory.create())
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://api.fanfou.com/")
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(client)
                .build();

        accountService = retrofit.create(AccountAPI.class);
        blockService = retrofit.create(BlockAPI.class);
        favoriteService = retrofit.create(FavoriteAPI.class);
        friendshipService = retrofit.create(FriendshipAPI.class);
        messageService = retrofit.create(MessageAPI.class);
        searchService = retrofit.create(SearchAPI.class);
        statusService = retrofit.create(StatusAPI.class);
        trendService = retrofit.create(TrendAPI.class);
        userService = retrofit.create(UserAPI.class);
    }

    public AccountAPI getAccountService() {
        return accountService;
    }

    public BlockAPI getBlockService() {
        return blockService;
    }

    public FavoriteAPI getFavoriteService() {
        return favoriteService;
    }

    public FriendshipAPI getFriendshipService() {
        return friendshipService;
    }

    public MessageAPI getMessageService() {
        return messageService;
    }

    public SearchAPI getSearchService() {
        return searchService;
    }

    public StatusAPI getStatusService() {
        return statusService;
    }

    public TrendAPI getTrendService() {
        return trendService;
    }

    public UserAPI getUserService() {
        return userService;
    }
}
