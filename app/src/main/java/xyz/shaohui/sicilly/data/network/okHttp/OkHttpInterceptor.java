package xyz.shaohui.sicilly.data.network.okHttp;

import android.text.TextUtils;
import android.util.Log;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import xyz.shaohui.sicilly.SicillyApplication;
import xyz.shaohui.sicilly.SicillyFactory;
import xyz.shaohui.sicilly.data.SPDataManager;
import xyz.shaohui.sicilly.data.network.auth.AuthService;
import xyz.shaohui.sicilly.data.network.auth.OAuthHelper;
import xyz.shaohui.sicilly.data.network.auth.OAuthToken;

/**
 * Created by shaohui on 16/8/19.
 */
public class OkHttpInterceptor implements Interceptor {
    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();

        //if (SicillyApplication.getToken() == null) {
        //    OAuthToken token = SPDataManager.getToken(SicillyApplication.getContext());
        //    if (token != null) {
        //        SicillyApplication.setToken(token);
        //    }
        //}

        String auth = OAuthHelper.buildAPIHeader(request.method(),
                request.url().url().toString(),
                new AuthService(), SicillyApplication.getToken());
        request = request.newBuilder()
                .header("Authorization",auth)
                .build();

        Log.i("TAG", auth);
        return chain.proceed(request);
    }
}
