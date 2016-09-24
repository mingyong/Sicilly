package xyz.shaohui.sicilly.data.network.api;

import java.util.List;

import okhttp3.RequestBody;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;
import rx.Observable;
import xyz.shaohui.sicilly.data.models.Conversation;
import xyz.shaohui.sicilly.data.models.Message;

/**
 * Created by shaohui on 16/8/19.
 */
public interface MessageAPI {

    @GET("direct_messages/conversation_list.json")
    Observable<List<Conversation>> conversationList(@Query("page")int page);

    @GET("direct_messages/conversation.json")
    Observable<List<Message>> messageList(@Query("id")String id,
                                          @Query("page")int page);

    @GET("direct_messages/inbox.json")
    Observable<List<Message>> inboxMessage(@Query("count") int count);

    @Multipart
    @POST("direct_messages/new.json")
    Observable<Message> sendMessage(@Part("user")RequestBody toId, @Part("text")RequestBody text);
}
