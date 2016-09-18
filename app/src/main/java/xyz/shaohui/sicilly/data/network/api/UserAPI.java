package xyz.shaohui.sicilly.data.network.api;

import java.util.List;
import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;
import xyz.shaohui.sicilly.data.models.Status;
import xyz.shaohui.sicilly.data.models.User;

/**
 * Created by shaohui on 16/8/19.
 */
public interface UserAPI {

    @GET("users/show.json?format=html")
    Observable<User> userInfoSelf();

    @GET("users/show.json?format=html")
    Observable<User> userInfoOther(@Query("id") String id);

    /**
     * 图片相关
     */
    @GET("photos/user_timeline.json?format=html")
    Observable<List<Status>> userPhotoSelf();

    @GET("photos/user_timeline.json?format=html")
    Observable<List<Status>> userPhotoSelfNext(@Query("since_id") String statusId);

    @GET("photos/user_timeline.json?format=html")
    Observable<List<Status>> userPhotoOther(@Query("id") String userId);

    @GET("photos/user_timeline.json?format=html")
    Observable<List<Status>> userPhotoOtherNext(@Query("id") String id, @Query("page") int page,
            @Query("since_id") int statusId);
}
