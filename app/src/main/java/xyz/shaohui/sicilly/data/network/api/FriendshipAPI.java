package xyz.shaohui.sicilly.data.network.api;

import okhttp3.RequestBody;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import rx.Observable;
import xyz.shaohui.sicilly.data.models.User;

/**
 * Created by shaohui on 16/8/19.
 */
public interface FriendshipAPI {

    @Multipart
    @POST("friendships/create.json")
    Observable<User> create(@Part("id") RequestBody id);

    @Multipart
    @POST("friendships/destroy.json")
    Observable<User> destroy(@Part("id") RequestBody id);

    @GET("friendships/exists.json")
    Observable<Boolean> exist(@Part("user_a") RequestBody idA, @Part("user_b") RequestBody idB);

    @Multipart
    @POST("friendships/deny.json")
    Observable<User> deny(@Part("id") RequestBody id);


}
