package xyz.shaohui.sicilly.data.database;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.squareup.sqlbrite.BriteDatabase;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;
import rx.Observable;
import xyz.shaohui.sicilly.SicillyApplication;
import xyz.shaohui.sicilly.data.models.Feedback;

/**
 * Created by shaohui on 16/9/24.
 */

public class FeedbackDbAccessor {

    public BriteDatabase mBriteDatabase;

    @Inject
    FeedbackDbAccessor(BriteDatabase briteDatabase) {
        mBriteDatabase = briteDatabase;
    }

    public Observable<List<Feedback>> loadFeedback() {
        return mBriteDatabase.createQuery(Feedback.TABLE_NAME, Feedback.SELECT_BY_UID,
                SicillyApplication.currentUId())
                .map(query -> {
                    Cursor cursor = query.run();
                    List<Feedback> result = new ArrayList<>();
                    while (cursor.moveToNext()) {
                        result.add(Feedback.MAPPER.map(cursor));
                    }
                    cursor.close();
                    return result;
                });
    }

    public void insertFeedback(Feedback feedback) {
        mBriteDatabase.insert(Feedback.TABLE_NAME,
                Feedback.FACTORY.marshal(feedback).asContentValues(),
                SQLiteDatabase.CONFLICT_REPLACE);
    }

    public void deleteAll() {
        mBriteDatabase.delete(Feedback.TABLE_NAME, "uid = ?",
                SicillyApplication.currentUId());
    }
}
