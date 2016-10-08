package xyz.shaohui.sicilly.data.network.auth;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import java.io.IOException;

import okhttp3.OkHttpClient;
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
import xyz.shaohui.sicilly.data.SPDataManager;

/**
 * Created by kpt on 16/2/22.
 */
public class AuthService {

    public final String ACCESS_TOKEN_URL = "http://fanfou.com/oauth/access_token/";

    public static final String CONSUMER_KEY = "25727328217674539431620d91127651";
    public static final String CONSUMER_SECRET = "e20b53cacb6a112a710dcc654dd84feb";

    public static String getConsumerKey() {
        return CONSUMER_KEY;
    }

    public static String getConsumerSecret() {
        return CONSUMER_SECRET;
    }

    public void getAccessToken(final Context context, String username, String password, final AuthListener listener) {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ACCESS_TOKEN_URL)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .client(new OkHttpClient())
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
                .subscribe(new Observer<ResponseBody>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        listener.failure();
                        Log.i("TAG", e.toString());
                        Toast.makeText(context, e.toString(), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onNext(ResponseBody response) {
                        try {
                            listener.end(OAuthToken.parse(response.string()));
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        response.close();
                    }
                });

    }

    public interface AuthListener {

        void begin();

        void end(OAuthToken token);

        void failure();
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
