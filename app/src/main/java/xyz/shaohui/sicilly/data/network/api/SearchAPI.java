package xyz.shaohui.sicilly.data.network.api;

import java.util.List;
import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;
import xyz.shaohui.sicilly.data.models.Status;
import xyz.shaohui.sicilly.data.models.User;
import xyz.shaohui.sicilly.data.models.UserListBean;

/**
 * Created by shaohui on 16/8/19.
 */
public interface SearchAPI {

    @GET("search/public_timeline.json?format=html&count=30&mode=lite")
    Observable<List<Status>> searchStatus(@Query("q")String q, @Query("since_id")Integer sinceId);

    @GET("search/users.json?mode=lite&format=html&count=30")
    Observable<UserListBean> searchUser(@Query("q")String q, @Query("page")int page);

}
