package xyz.shaohui.sicilly.data.services.api;

import com.google.gson.JsonArray;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by kpt on 16/2/25.
 */
public interface SearchAPI {

    @GET("search/public_timeline.json?format=html&mode=lite")
    Observable<JsonArray> searchAll(@Query("q")String q,
                                    @Query("count")int count,
                                    @Query("page")int page);

    @GET("search/users.json?format=html&mode=lite")
    Observable<JsonArray> searchUser(@Query("q")String q,
                                     @Query("count")int count,
                                     @Query("page")int page);
}
