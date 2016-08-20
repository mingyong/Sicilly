package xyz.shaohui.sicilly.data.network.api;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;
import xyz.shaohui.sicilly.data.models.User;

/**
 * Created by shaohui on 16/8/19.
 */
public interface UserAPI {

    @GET("users/show.json?format=html")
    Observable<User> userInfoSelf();

    @GET("users/show.json?format=html")
    Observable<User> userInfoOther(@Query("id")String id);

}
