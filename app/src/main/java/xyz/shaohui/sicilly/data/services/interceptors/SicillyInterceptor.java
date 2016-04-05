package xyz.shaohui.sicilly.data.services.interceptors;

import android.util.Log;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import xyz.shaohui.sicilly.SicillyApplication;
import xyz.shaohui.sicilly.SicillyFactory;
import xyz.shaohui.sicilly.data.preferences.TokenSP;
import xyz.shaohui.sicilly.data.services.auth.AuthService;
import xyz.shaohui.sicilly.data.services.auth.OAuthHelper;

/**
 * Created by kpt on 16/2/23.
 */
public class SicillyInterceptor implements Interceptor {
    @Override
    public Response intercept(Chain chain) throws IOException {
        Request originalRequest = chain.request();

        if (SicillyFactory.getToken().equals(null)) {
            SicillyFactory.setToken(TokenSP.accessToken(SicillyApplication.getContext()));
        }

        String auth = OAuthHelper.buildAPIHeader(originalRequest.method(),
                originalRequest.url().url().toString(),
                new AuthService(), SicillyFactory.getToken());
        Request request = originalRequest.newBuilder()
                .header("Authorization",auth)
                .build();

        Log.i("TAG", auth);

        return chain.proceed(request);
    }
}
