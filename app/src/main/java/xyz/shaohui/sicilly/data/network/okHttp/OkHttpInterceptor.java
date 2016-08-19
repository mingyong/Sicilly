package xyz.shaohui.sicilly.data.network.okHttp;

import android.text.TextUtils;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import xyz.shaohui.sicilly.SicillyApplication;

/**
 * Created by shaohui on 16/8/19.
 */
public class OkHttpInterceptor implements Interceptor {
    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();

        Request.Builder newBuilder = request.newBuilder();
        if (!TextUtils.isEmpty(SicillyApplication.getToken())) {
            newBuilder.addHeader("Authorization", SicillyApplication.getToken());
        }

        request = newBuilder.build();
        return chain.proceed(request);
    }
}
