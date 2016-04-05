package xyz.shaohui.sicilly.data.services.api;

import com.google.gson.JsonArray;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by kpt on 16/2/25.
 */
public interface SearchAPI {

    @GET("search/public_timeline.json?format=html&count=30")
    Observable<JsonArray> searchStatus(@Query("q")String q);

    @GET("search/public_timeline.json?format=html&count=30")
    Observable<JsonArray> searchStatusMore(@Query("q")String q, @Query("since_id")String id);

    @GET("search/users.json?format=html&count=30")
    Observable<JsonArray> searchUser(@Query("q")String q, @Query("page")int page);
}
