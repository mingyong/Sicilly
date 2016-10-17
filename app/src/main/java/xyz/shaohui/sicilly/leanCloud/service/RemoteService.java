package xyz.shaohui.sicilly.leanCloud.service;

import android.os.Build;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import xyz.shaohui.sicilly.BuildConfig;
import xyz.shaohui.sicilly.leanCloud.LeanCloudAPI;
import xyz.shaohui.sicilly.leanCloud.LeanCloudService;
import xyz.shaohui.sicilly.leanCloud.model.ActiveUser;
import xyz.shaohui.sicilly.leanCloud.model.RemoteFeedback;
import xyz.shaohui.sicilly.utils.ErrorUtils;
import xyz.shaohui.sicilly.utils.RxUtils;

/**
 * Created by shaohui on 16/10/6.
 */

public class RemoteService {

    public static void activeUser(String userId, String userName, String regId) {
        ActiveUser user = new ActiveUser(userId, userName, regId);
        Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
        LeanCloudAPI service = LeanCloudService.getInstance();
        service.queryActiveUser(gson.toJson(user))
                .subscribeOn(Schedulers.io())
                .flatMap(activeUserLeanCloudResult -> {
                    if (activeUserLeanCloudResult.getResults().isEmpty()) {
                        return service.addActiveUser(user);
                    } else {
                        return Observable.just(activeUserLeanCloudResult);
                    }
                })
                .subscribe(activeUserLeanCloudResult -> {

                }, RxUtils.ignoreNetError);
    }

    public static void sendRemoteFeedback(String uId, String uName, String regId, String text) {
        RemoteFeedback feedback =
                RemoteFeedback.create(uId, uName, regId, text, Build.MODEL, Build.BRAND,
                        String.valueOf(Build.VERSION.SDK_INT), Build.VERSION.RELEASE,
                        BuildConfig.VERSION_NAME);
        LeanCloudAPI service = LeanCloudService.getInstance();
        service.sendFeedback(feedback)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(leanCloudNew -> {

                }, ErrorUtils::catchException);
    }

    public static void requestQiniuToken() {
        LeanCloudAPI service = LeanCloudService.getInstance();
        service.requestQiniuToken()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(qiniuTokenLeanCloudResult -> {

                }, throwable -> {

                });
    }
}
