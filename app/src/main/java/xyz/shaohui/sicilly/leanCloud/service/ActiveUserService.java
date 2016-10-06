package xyz.shaohui.sicilly.leanCloud.service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import xyz.shaohui.sicilly.leanCloud.LeanCloudAPI;
import xyz.shaohui.sicilly.leanCloud.LeanCloudService;
import xyz.shaohui.sicilly.leanCloud.model.ActiveUser;
import xyz.shaohui.sicilly.utils.RxUtils;

/**
 * Created by shaohui on 16/10/6.
 */

public class ActiveUserService {

    public static void activeUser(String userId, String userName) {
        ActiveUser user = new ActiveUser(userId, userName);
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
}
