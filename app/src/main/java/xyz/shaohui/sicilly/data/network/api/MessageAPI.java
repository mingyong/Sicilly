package xyz.shaohui.sicilly.data.network.api;

import java.util.List;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;
import xyz.shaohui.sicilly.data.models.Conversation;

/**
 * Created by shaohui on 16/8/19.
 */
public interface MessageAPI {

    @GET("direct_messages/conversation_list.json")
    Observable<List<Conversation>> conversationList(@Query("page")int page);

}
