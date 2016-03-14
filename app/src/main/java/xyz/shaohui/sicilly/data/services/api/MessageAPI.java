package xyz.shaohui.sicilly.data.services.api;

import com.google.gson.JsonArray;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by kpt on 16/3/13.
 */
public interface MessageAPI {

    @GET("direct_messages/conversation_list.json?mode=lite&count=30")
    Observable<JsonArray> couversationList(@Query("page")int page);

}
