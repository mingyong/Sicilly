package xyz.shaohui.sicilly.data.network.api;

import java.util.List;
import okhttp3.RequestBody;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;
import rx.Observable;
import xyz.shaohui.sicilly.data.models.FriendshipDetail;
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
    Observable<Boolean> exist(@Query("user_a")String idA, @Query("user_b")String idB);

    @GET("friendships/requests.json?count=60&mode=lite")
    Observable<List<User>> requestList(@Query("page")int page);

    @Multipart
    @POST("friendships/deny.json?mode=lite")
    Observable<User> deny(@Part("id") RequestBody id);

    @Multipart
    @POST("friendships/accept.json?mode=lite")
    Observable<User> accept(@Part("id") RequestBody id);

    @GET("friendships/show.json")
    Observable<FriendshipDetail> showDetail(@Query("source_id") String sourceId,
            @Query("target_id") String targetId);


}
