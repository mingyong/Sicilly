package xyz.shaohui.sicilly.data.services.api;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import okhttp3.RequestBody;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by kpt on 16/3/26.
 */
public interface FriendshipAPI {

    @Multipart
    @POST("friendships/create.json?mode=lite")
    Observable<JsonObject> create(@Part("id")RequestBody user_id);

    @Multipart
    @POST("friendships/destroy.json?mode=lite")
    Observable<JsonObject> destroy(@Part("id")RequestBody user_id);

    @Multipart
    @POST("friendships/deny.json?mode=lite")
    Observable<JsonObject> deny(@Part("id")RequestBody user_id);

    @Multipart
    @POST("friendships/accept.json?mode=lite")
    Observable<JsonObject> accept(@Part("id")RequestBody user_id);

    @GET("friendships/requests.json?mode=lite&format=html")
    Observable<JsonArray> requestList(@Query("page")int page, @Query("count")int count);

}
