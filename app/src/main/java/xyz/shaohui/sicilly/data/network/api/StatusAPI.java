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

    @GET("statuses/home_timeline.json?format=html")
    Observable<List<Status>> homeStatusNext(@Query("since_id")String sinceId,
                                            @Query("page")int page);
}
