package xyz.shaohui.sicilly.leanCloud;

import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import rx.Observable;
import xyz.shaohui.sicilly.leanCloud.model.ActiveUser;
import xyz.shaohui.sicilly.leanCloud.model.LeanCloudNew;
import xyz.shaohui.sicilly.leanCloud.model.LeanCloudResult;

/**
 * Created by shaohui on 16/10/6.
 */

public interface LeanCloudAPI {

    /**
     * 激活用户
     */

    @GET("classes/ActiveUser")
    Observable<LeanCloudResult<ActiveUser>> queryActiveUser(@Query("where") String query);

    @POST("classes/ActiveUser")
    Observable<LeanCloudNew> addActiveUser(@Body ActiveUser body);
}
