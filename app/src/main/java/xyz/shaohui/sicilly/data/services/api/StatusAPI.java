package xyz.shaohui.sicilly.data.services.api;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.Map;

import okhttp3.RequestBody;
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

    @GET("statuses/home_timeline.json?format=html&mode=lite")
    Observable<JsonArray> homeData(@Query("count")int count, @Query("page")int page);

    @GET("statuses/public_timeline.json?format=html&mode=lite")
    Observable<JsonArray> publicData(@Query("count")int count, @Query("since_id")String id);

    @GET("statuses/mentions.json?format=html&mode=lite")
    Observable<JsonArray> aboutMeData(@Query("count")int count, @Query("page")int page);

    @GET("statuses/show.json?format=html&mode=lite")
    Observable<JsonObject> showStatus(@Query("id")String id);

    @GET("statuses/context_timeline.json?format=html&mode=lite")
    Observable<JsonArray> contextTimeline(@Query("id")String id);

    @Multipart
    @POST("statuses/update.json")
    Observable<JsonObject> createStatus(@Part("status")RequestBody status);

    @Multipart
    @POST("photos/upload.json")
    Observable<JsonObject> createStatusWithPhoto(@PartMap Map<String, RequestBody> map);

    @POST("statuses/update.json")
    Observable<JsonObject> replyStatus(
            @Query("status")String status, @Query("in_reply_to_status_id")String id, @Query("source")String source);

    @Multipart
    @POST("statuses/update.json")
    Observable<JsonObject> repostStatus(@Part("status")RequestBody status, @Part("repost_status_id")RequestBody id);

    @Multipart
    @POST("statuses/update.json")
    Observable<JsonObject> repostStatus(@Part("status")RequestBody body);

    @POST("statuses/destroy.json")
    Observable<JsonObject> destroyStatus(@Query("id")String id);


}
