package xyz.shaohui.sicilly.views.friend_list.di;

import dagger.Module;
import dagger.Provides;
import javax.inject.Named;
import xyz.shaohui.sicilly.views.friend_list.FriendListPresenterImpl;
import xyz.shaohui.sicilly.views.friend_list.mvp.FriendListMVP;

/**
 * Created by shaohui on 16/10/14.
 */

@Module
public class FriendListModule {

    public static final String NAME_DATA_TYPE = "data_type";

    public static final String NAME_USER_ID = "user_id";

    public static final String NAME_VIEW_TYPE = "view_type";

    private int dataType;
    private String userId;
    private int viewType;

    public FriendListModule(String userId, int dataType, int viewType) {
        this.dataType = dataType;
        this.userId = userId;
        this.viewType = viewType;
    }

    @Provides
    FriendListMVP.Presenter providePresenter(FriendListPresenterImpl presenter) {
        return presenter;
    }

    @Provides
    @Named(NAME_USER_ID)
    String provideUserId() {
        return userId;
    }

    @Provides
    @Named(NAME_DATA_TYPE)
    int provideDateType() {
        return dataType;
    }

    @Provides
    @Named(NAME_VIEW_TYPE)
    int provideViewType() {
        return viewType;
    }
}
