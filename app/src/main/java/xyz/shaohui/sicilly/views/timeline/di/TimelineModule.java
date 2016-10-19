package xyz.shaohui.sicilly.views.timeline.di;

import dagger.Module;
import dagger.Provides;
import javax.inject.Named;
import xyz.shaohui.sicilly.views.timeline.TimelinePresenterImpl;
import xyz.shaohui.sicilly.views.timeline.mvp.TimelineMVP;

/**
 * Created by shaohui on 2016/10/19.
 */

@Module
public class TimelineModule {

    public static final String TIMELINE_USER_ID = "timeline_user_id";

    public static final String TIMELINE_DATA_TYPE = "timeline_data_type";

    private String mUserId;

    private int mDataType;

    public TimelineModule(String userId, int dataType) {
        mUserId = userId;
        mDataType = dataType;
    }

    @Provides
    TimelineMVP.Presenter provideTimelinePresenter(TimelinePresenterImpl presenter) {
        return presenter;
    }

    @Provides
    @Named(TIMELINE_USER_ID)
    String provideUserId() {
        return mUserId;
    }

    @Provides
    @Named(TIMELINE_DATA_TYPE)
    int provideDataType() {
        return mDataType;
    }

}
