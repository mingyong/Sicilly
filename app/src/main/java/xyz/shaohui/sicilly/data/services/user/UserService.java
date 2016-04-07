package xyz.shaohui.sicilly.data.services.user;

import android.net.Uri;

import com.google.gson.JsonObject;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Retrofit;
import rx.Observer;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import xyz.shaohui.sicilly.SicillyFactory;
import xyz.shaohui.sicilly.data.services.RetrofitService;
import xyz.shaohui.sicilly.utils.MyToast;

/**
 * Created by kpt on 16/2/24.
 */
public class UserService {

    /**
     * 添加取消收藏
     * @param id
     * @param callBack
     */
    public static void createFavorite(String id, final CallBack callBack) {

        RetrofitService service = SicillyFactory.getRetrofitService();
        service.getUserService().createFavorite(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<JsonObject>() {
                    @Override
                    public void onCompleted() {
                        callBack.success();
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        callBack.failure();
                    }

                    @Override
                    public void onNext(JsonObject jsonObject) {

                    }
                });
    }

    public static void destroyFavorite(String id, final UserService.CallBack callBack) {

        RetrofitService service = SicillyFactory.getRetrofitService();
        service.getUserService().destroyFavorite(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<JsonObject>() {
                    @Override
                    public void onCompleted() {
                        callBack.success();
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        callBack.failure();
                    }

                    @Override
                    public void onNext(JsonObject jsonObject) {

                    }
                });

    }

    public static void createStatus(String text, final UserService.CallBack callBack) {
        RetrofitService service = SicillyFactory.getRetrofitService();
        service.getStatusService().createStatus(toRequestBody(text))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<JsonObject>() {
                    @Override
                    public void onCompleted() {
                        callBack.success();
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        callBack.failure();
                    }

                    @Override
                    public void onNext(JsonObject jsonObject) {

                    }
                });

    }

    public static void createStatusImg(Uri imgUri, String text, final UserService.CallBack callBack) {

        RetrofitService service = SicillyFactory.getRetrofitService();

        File file = new File(imgUri.getPath());
        RequestBody photo = RequestBody.create(MediaType.parse("image/*"), file);

        service.getStatusService().createStatusWithPhoto(photo, toRequestBody(text))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<JsonObject>() {
                    @Override
                    public void onCompleted() {
                        callBack.success();
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        callBack.failure();
                    }

                    @Override
                    public void onNext(JsonObject jsonObject) {

                    }
                });

    }

    public static void repostStatus(String text, String repostStatusId, final UserService.CallBack callBack) {
        RetrofitService service = SicillyFactory.getRetrofitService();
        service.getStatusService().repostStatus(toRequestBody(text), toRequestBody(repostStatusId))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<JsonObject>() {
                    @Override
                    public void onCompleted() {
                        callBack.success();
                    }

                    @Override
                    public void onError(Throwable e) {
                        callBack.failure();
                        e.printStackTrace();
                    }

                    @Override
                    public void onNext(JsonObject jsonObject) {

                    }
                });
    }

    public static void replyStatus(String text, String replyStatusId,
                                   final UserService.CallBack callBack) {
        RetrofitService service = SicillyFactory.getRetrofitService();
        service.getStatusService().replyStatus(toRequestBody(text), toRequestBody(replyStatusId))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<JsonObject>() {
                    @Override
                    public void onCompleted() {
                        callBack.success();
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        callBack.failure();
                    }

                    @Override
                    public void onNext(JsonObject jsonObject) {

                    }
                });
    }

    public static void createMessage(String id, String text, final CallBack callBack) {
        RetrofitService service = SicillyFactory.getRetrofitService();
        service.getMessageService().newMessage(toRequestBody(id), toRequestBody(text))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<JsonObject>() {
                    @Override
                    public void onCompleted() {
                        callBack.success();
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        callBack.failure();
                    }

                    @Override
                    public void onNext(JsonObject jsonObject) {

                    }
                });
    }

    public static void createFollow(String userId, final UserService.CallBack callBack) {
        RetrofitService service = SicillyFactory.getRetrofitService();
        service.getFriendshipService().create(toRequestBody(userId))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<JsonObject>() {
                    @Override
                    public void onCompleted() {
                        callBack.success();
                    }

                    @Override
                    public void onError(Throwable e) {
                        callBack.failure();
                        e.printStackTrace();
                    }

                    @Override
                    public void onNext(JsonObject jsonObject) {

                    }
                });
    }

    public static void destroyFollow(String userId, final UserService.CallBack callBack) {
        RetrofitService service = SicillyFactory.getRetrofitService();
        service.getFriendshipService().destroy(toRequestBody(userId))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<JsonObject>() {
                    @Override
                    public void onCompleted() {
                        callBack.success();
                    }

                    @Override
                    public void onError(Throwable e) {
                        callBack.failure();
                        e.printStackTrace();
                    }

                    @Override
                    public void onNext(JsonObject jsonObject) {

                    }
                });
    }

    public static void deleteStatus(String statusId, final UserService.CallBack callBack) {
        RetrofitService service = SicillyFactory.getRetrofitService();
        service.getStatusService().destroyStatus(toRequestBody(statusId))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<JsonObject>() {
                    @Override
                    public void onCompleted() {
                        callBack.success();
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        callBack.failure();
                    }

                    @Override
                    public void onNext(JsonObject jsonObject) {

                    }
                });
    }

    public static RequestBody toRequestBody(String str) {
        RequestBody body = RequestBody.create(MediaType.parse("text/plain"), str);
        return body;
    }

    public interface CallBack {

        public void success();

        public void failure();
    }

}
