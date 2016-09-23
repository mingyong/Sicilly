package xyz.shaohui.sicilly.data.network.api;

import okhttp3.RequestBody;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import rx.Observable;
import xyz.shaohui.sicilly.data.models.FanNotification;
import xyz.shaohui.sicilly.data.models.User;

/**
 * Created by shaohui on 16/8/19.
 */
public interface AccountAPI {

    @GET("account/verify_credentials.json")
    Observable<User> verifyUser();

    @Multipart
    @POST("account/update_profile_image.json")
    Observable<User> udpateProfileImage(
            @Part("image\"; filename=\"image.jpg\" ") RequestBody image);

    @GET("account/notification.json")
    Observable<FanNotification> notification();
}
