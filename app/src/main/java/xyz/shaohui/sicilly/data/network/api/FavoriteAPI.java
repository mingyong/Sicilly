package xyz.shaohui.sicilly.data.network.api;

import java.util.List;
import retrofit2.http.Field;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;
import xyz.shaohui.sicilly.data.models.Status;

/**
 * Created by shaohui on 16/8/19.
 */
public interface FavoriteAPI {

    @POST("favorites/create/{id}.json")
    Observable<Status> createFavorite(@Path("id") String id);

    @POST("favorites/destroy/{id}.json")
    Observable<Status> destroyFavorite(@Path("id")String id);

    @GET("favorites/{id}.json?count=60&format=html&mode=lite")
    Observable<List<Status>> favoriteStatusList(@Path("id")String id, @Query("page")int page);
}
