package xyz.shaohui.sicilly.data.network.api;

import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import retrofit2.http.Url;
import rx.Observable;

/**
 * Created by shaohui on 16/9/24.
 */

public interface SimpleAPI {

    @POST()
    Observable<Void> sendFeedback(@Url String url, @Body RequestBody text);

}
