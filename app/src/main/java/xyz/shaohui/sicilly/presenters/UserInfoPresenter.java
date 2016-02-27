package xyz.shaohui.sicilly.presenters;

import com.google.gson.JsonObject;

import retrofit2.Retrofit;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;
import xyz.shaohui.sicilly.SicillyFactory;
import xyz.shaohui.sicilly.data.models.User;
import xyz.shaohui.sicilly.data.services.RetrofitService;
import xyz.shaohui.sicilly.ui.activities.UserInfoActivity;

/**
 * Created by kpt on 16/2/25.
 */
public class UserInfoPresenter {

    private UserInfoActivity activity;
    private RetrofitService service;

    private String id;

    public UserInfoPresenter(UserInfoActivity activity, String id) {
        this.activity = activity;
        this.id = id;
        service = SicillyFactory.getRetrofitService();
    }

    public void fetchBaseInfo() {
        service.getUserService().showUser(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnRequest(new Action1<Long>() {
                    @Override
                    public void call(Long aLong) {
                        activity.showDilog();
                    }
                })
                .doOnUnsubscribe(new Action0() {
                    @Override
                    public void call() {
                        activity.dismissDialog();
                    }
                })
                .observeOn(Schedulers.io())
                .map(new Func1<JsonObject, User>() {
                    @Override
                    public User call(JsonObject jsonObject) {
                        return User.toObject(jsonObject);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<User>() {
                    @Override
                    public void call(User user) {
                        activity.setUpInfo(user);
                    }
                });
    }

    private void setUpInfo(User user) {

    }

}
