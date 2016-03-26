package xyz.shaohui.sicilly.data.services.api;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import okhttp3.RequestBody;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by kpt on 16/3/13.
 */
public interface MessageAPI {

    @GET("direct_messages/conversation_list.json?count=30")
    Observable<JsonArray> conversationList(@Query("page")int page);

    @GET("direct_messages/conversation.json?count=30")
    Observable<JsonArray> conversation(@Query("id")String id, @Query("page")int page);

    @Multipart
    @POST("direct_messages/new.json")
    Observable<JsonObject> newMessage(@Part("user")RequestBody id, @Part("text")RequestBody text);

}
