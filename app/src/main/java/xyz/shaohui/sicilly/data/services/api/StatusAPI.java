package xyz.shaohui.sicilly.data.services.api;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.io.File;
import java.util.Map;

import okhttp3.RequestBody;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by kpt on 16/2/22.
 */
public interface StatusAPI {

    @GET("statuses/home_timeline.json?format=html")
    Observable<JsonArray> homeData(@Query("count")int count, @Query("page")int page);

    @GET("statuses/public_timeline.json?format=html")
    Observable<JsonArray> publicData(@Query("count")int count);

    @GET("statuses/public_timeline.json?format=html")
    Observable<JsonArray> publicDataMore(@Query("count")int count, @Query("since_id")String id);

    @GET("statuses/mentions.json?format=html")
    Observable<JsonArray> aboutMeData(@Query("count")int count, @Query("page")int page);

    @GET("statuses/show.json?format=html")
    Observable<JsonObject> showStatus(@Query("id")String id);

    @GET("statuses/context_timeline.json?format=html")
    Observable<JsonArray> contextTimeline(@Query("id")String id);

    @Multipart
    @POST("statuses/update.json")
    Observable<JsonObject> createStatus(@Part("status")RequestBody status);

    @Multipart
    @POST("photos/upload.json")
    Observable<JsonObject> createStatusWithPhoto(@Part("photo")RequestBody photo, @Part("status")RequestBody status);

    @Multipart
    @POST("statuses/update.json")
    Observable<JsonObject> replyStatus(@Part("status")RequestBody status,
                                       @Part("in_reply_to_status_id")RequestBody id);

    @Multipart
    @POST("statuses/update.json")
    Observable<JsonObject> repostStatus(@Part("status")RequestBody status,
                                        @Part("repost_status_id")RequestBody id);

    @Multipart
    @POST("statuses/update.json")
    Observable<JsonObject> repostStatus(@Part("status")RequestBody body);

    @Multipart
    @POST("statuses/destroy.json")
    Observable<JsonObject> destroyStatus(@Part("id")RequestBody id);


}
