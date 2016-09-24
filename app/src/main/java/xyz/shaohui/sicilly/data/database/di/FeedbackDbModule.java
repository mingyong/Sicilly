package xyz.shaohui.sicilly.data.database.di;

import dagger.Module;
import dagger.Provides;
import xyz.shaohui.sicilly.data.database.FeedbackDbAccessor;

/**
 * Created by shaohui on 16/9/24.
 */

@Module
public class FeedbackDbModule {
    @Provides
    FeedbackDbAccessor provideFeedbackDb(FeedbackDbAccessor accessor) {
        return accessor;
    }
}
