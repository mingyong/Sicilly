package xyz.shaohui.sicilly.data.network.api;

import java.util.List;
import okhttp3.RequestBody;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;
import rx.Observable;
import xyz.shaohui.sicilly.data.models.Status;

/**
 * Created by shaohui on 16/8/4.
 */
public interface StatusAPI {

    @GET("statuses/home_timeline.json?format=html")
    Observable<List<Status>> homeStatus();

    @GET("statuses/home_timeline.json?format=html")
    Observable<List<Status>> homeStatusNext(@Query("page") int page, @Query("since_id") int id);

    @GET("statuses/user_timeline.json?format=html")
    Observable<List<Status>> userTimeline(@Query("id") String userId);

    @GET("statuses/user_timeline.json?format=html")
    Observable<List<Status>> userTimelineNext(@Query("id") String userId, @Query("page") int page,
            @Query("since_id") int id);

    @GET("statuses/mentions.json?format=html")
    Observable<List<Status>> mentionsStatus();

    @GET("statuses/mentions.json?format=html")
    Observable<List<Status>> mentionsStatus(@Query("count")int count);

    @GET("statuses/mentions.json?format=html")
    Observable<List<Status>> mentionsStatusNext(@Query("page") int page, @Query("since_id")int id);

    @Multipart
    @POST("statuses/update.json")
    Observable<Status> createStatus(@Part("status") RequestBody text,
            @Part("in_reply_to_status_id") String reply_status_id,
            @Part("repost_status_id") String repost_status_id);

    @Multipart
    @POST("photos/upload.json?format=html")
    Observable<Status> createStatusWithPhoto(@Part("status") RequestBody status,
            @Part("photo\"; filename=\"image.jpg\" ") RequestBody photo);

    @Multipart
    @POST("statuses/destroy.json")
    Observable<Status> destroyStatus(@Part("id") RequestBody id);

    @GET("statuses/context_timeline.json")
    Observable<List<Status>> context(@Query("id")String id);
}
