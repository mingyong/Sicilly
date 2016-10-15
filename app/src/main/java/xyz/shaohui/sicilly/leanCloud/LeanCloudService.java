package xyz.shaohui.sicilly.leanCloud;

import javax.inject.Inject;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import xyz.shaohui.sicilly.utils.HashUtils;

/**
 * Created by shaohui on 16/10/6.
 */

public class LeanCloudService {

    private static Retrofit mRetrofit;

    @Inject
    LeanCloudService(Retrofit retrofit) {

        mRetrofit = retrofit;
    }

    static final String APP_ID = "qlcD2r0YxFoqsxms6TTfR7qq-gzGzoHsz";

    static final String APP_KEY = "vgzabsPtGHicas7K3c1L6Gwc";

    private static final String LEAN_CLOUD_URL = "https://api.leancloud.cn/1.1/";

    public static LeanCloudAPI getInstance() {
        if (mRetrofit == null) {
            mRetrofit = createRetrofit();
        }
        return mRetrofit.create(LeanCloudAPI.class);
    }

    private static Retrofit createRetrofit() {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor)
                .addInterceptor(new LeanCloudInterceptor())
                .build();
        return new Retrofit.Builder().baseUrl(LEAN_CLOUD_URL)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();
    }

    static String buildSignKey() {
        long timestamp = System.currentTimeMillis();
        String md5Key = HashUtils.forMD5(APP_KEY + timestamp);
        return String.format("%s,%s", md5Key, timestamp);
    }

}
