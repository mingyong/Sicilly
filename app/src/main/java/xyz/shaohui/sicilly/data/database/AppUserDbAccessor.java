package xyz.shaohui.sicilly.data.database;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.squareup.sqlbrite.BriteDatabase;
import com.squareup.sqlbrite.SqlBrite;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;
import rx.Observable;
import xyz.shaohui.sicilly.SicillyApplication;
import xyz.shaohui.sicilly.data.models.AppUser;

/**
 * Created by shaohui on 16/9/19.
 */

public class AppUserDbAccessor {

    private BriteDatabase mBriteDatabase;

    @Inject
    AppUserDbAccessor(BriteDatabase briteDatabase) {
        mBriteDatabase = briteDatabase;
    }

    public Observable<List<AppUser>> fetchAllUser() {
        return mBriteDatabase.createQuery(AppUser.TABLE_NAME, AppUser.SELECT_ALL).map(query -> {
            List<AppUser> appUsers = new ArrayList<>();
            Cursor cursor = query.run();
            while (cursor != null && cursor.moveToNext()) {
                appUsers.add(AppUser.MAPPER.map(cursor));
            }
            return appUsers;
        });
    }

    public Observable<Cursor> selectCurrentUser() {
        return mBriteDatabase.createQuery(AppUser.TABLE_NAME, AppUser.SELECT_CURRENT)
                .map(SqlBrite.Query::run);
    }

    public void insertUser(AppUser user) {
        mBriteDatabase.insert(AppUser.TABLE_NAME, AppUser.FACTORY.marshal(user).asContentValues(),
                SQLiteDatabase.CONFLICT_REPLACE);
    }

    public void deleteUser(String userId) {
        mBriteDatabase.delete(AppUser.TABLE_NAME, AppUser.DELETE_BY_ID, userId);
    }

    public void switchActiveUser(AppUser origin, AppUser target) {
        AppUser originUser = origin.updateActive();
        AppUser targetUser = target.updateActive();
        BriteDatabase.Transaction transaction = mBriteDatabase.newTransaction();
        mBriteDatabase.update(AppUser.TABLE_NAME,
                AppUser.FACTORY.marshal(originUser).asContentValues(), "id = ?", originUser.id());
        mBriteDatabase.update(AppUser.TABLE_NAME,
                AppUser.FACTORY.marshal(targetUser).asContentValues(), "id = ?", targetUser.id());
        transaction.markSuccessful();
        transaction.end();
    }

    public void updateActiveUser(AppUser origin, AppUser target) {
        AppUser originUser = origin.updateActive();
        BriteDatabase.Transaction transaction = mBriteDatabase.newTransaction();
        mBriteDatabase.insert(AppUser.TABLE_NAME, AppUser.FACTORY.marshal(target).asContentValues(),
                SQLiteDatabase.CONFLICT_REPLACE);
        mBriteDatabase.update(AppUser.TABLE_NAME,
                AppUser.FACTORY.marshal(originUser).asContentValues(), "id = ?", originUser.id());
        transaction.markSuccessful();
        transaction.end();
    }
}
