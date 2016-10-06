package xyz.shaohui.sicilly.leanCloud;

import android.util.Log;
import java.io.IOException;
import okhttp3.Headers;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import xyz.shaohui.sicilly.BuildConfig;

/**
 * Created by shaohui on 16/10/6.
 */

public class LeanCloudInterceptor implements Interceptor {
    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        request = request.newBuilder()
                .header("X-LC-Id", LeanCloudService.APP_ID)
                .header("X-LC-Key", LeanCloudService.APP_KEY)
                .build();
        if (BuildConfig.DEBUG) {
            Log.i("TAG", LeanCloudService.buildSignKey());
            Log.i("TAG", request.url().toString());
            Log.i("TAG", request.method());
        }
        Response response = chain.proceed(request);
        Log.i("TAG", response.message());
        return response;
    }
}
