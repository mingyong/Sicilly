package xyz.shaohui.sicilly.data.services.auth;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.http.GET;
import retrofit2.http.Header;
import rx.Observable;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.schedulers.Schedulers;
import xyz.shaohui.sicilly.data.preferences.TokenSP;

/**
 * Created by kpt on 16/2/22.
 */
public class AuthService {

    private final String ACCESS_TOKEN_URL = "http://fanfou.com/oauth/access_token/";

    private static final String CONSUMER_KEY = "25727328217674539431620d91127651";
    private static final String CONSUMER_SECRET = "e20b53cacb6a112a710dcc654dd84feb";

    public static String getConsumerKey() {
        return CONSUMER_KEY;
    }

    public static String getConsumerSercret() {
        return CONSUMER_SECRET;
    }

    public void getAccessToken(final Context context, String username, String password, final AuthListener listener) {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ACCESS_TOKEN_URL)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();

        AuthAPI authAPI = retrofit.create(AuthAPI.class);

        authAPI.auth(getAuth(username, password))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(new Action0() {
                    @Override
                    public void call() {
                        listener.begin();
                    }
                })
                .subscribeOn(AndroidSchedulers.mainThread())
                .doOnNext(new Action1<ResponseBody>() {
                    @Override
                    public void call(ResponseBody responseBody) {
                        try {
                            TokenSP.saveToken(context, OAuthToken.parse(responseBody.string()));
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                })
                .subscribeOn(Schedulers.io())
                .subscribe(new Observer<ResponseBody>() {
                    @Override
                    public void onCompleted() {
                        listener.end();
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.i("TAG", e.toString());
                        Toast.makeText(context, e.toString(), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onNext(ResponseBody response) {

                    }
                });

    }

    public interface AuthListener {

        void begin();

        void end();
    }

    private String getAuth(String username, String password) {
        final String authorization = OAuthHelper.buildXAuthHeader(username,
                password, "GET",
                this.ACCESS_TOKEN_URL, this);
        return authorization;
    }

    public interface AuthAPI {

        @GET("/oauth/access_token/")
        Observable<ResponseBody> auth(@Header("Authorization") String authorization);

    }




}
