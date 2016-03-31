package xyz.shaohui.sicilly.data.services.api;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.Map;

import okhttp3.RequestBody;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PartMap;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by kpt on 16/2/24.
 */
public interface UserAPI {

    @GET("account/verify_credentials.json?format=html")
    Observable<JsonObject> personalInfo();

    @GET("users/show.json?format=html&")
    Observable<JsonObject> showUser(@Query("id")String id);

    @GET("statuses/user_timeline.json?format=html&count=30")
    Observable<JsonArray> homePage(@Query("page")int page);

    @GET("statuses/user_timeline.json?format=html&count=30")
    Observable<JsonArray> userTimeline(@Query("id")String id, @Query("page")int page);

    @GET("photos/user_timeline.json?format=html")
    Observable<JsonArray> userPhoto(@Query("id")String id, @Query("count")int count, @Query("page")int page);

    @POST("favorites/destroy/id.json")
    Observable<JsonObject> destroyFavorite(@Query("id")String id);

    @POST("favorites/create/id.json")
    Observable<JsonObject> createFavorite(@Query("id")String id);

    @GET("photos/user_timeline.json?count=30")
    Observable<JsonArray> showPhoto(@Query("id")String id, @Query("page")int page);

    @Multipart
    @POST("")
    Observable<JsonObject> createStatus(@PartMap Map<String, RequestBody> map);
}
