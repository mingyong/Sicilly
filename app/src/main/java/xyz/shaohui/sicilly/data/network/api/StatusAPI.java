package xyz.shaohui.sicilly.data.network.api;

import java.util.List;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;
import xyz.shaohui.sicilly.data.models.Status;

/**
 * Created by shaohui on 16/8/4.
 */
public interface StatusAPI {

    @GET("statuses/home_timeline.json?format=html")
    Observable<List<Status>> homeStatus();

    @GET("statuses/home_timeline.json?format=html&count=30")
    Observable<List<Status>> homeStatusNext(@Query("page")int page, @Query("since_id")int id);

    @GET("statuses/user_timeline.json?format=html")
    Observable<List<Status>> userTimeline(@Query("id")String userId,
                                          @Query("page")int page);

    @GET("statuses/mentions.json?format=html")
    Observable<List<Status>> mentionsStatus(@Query("page")int page);
}
